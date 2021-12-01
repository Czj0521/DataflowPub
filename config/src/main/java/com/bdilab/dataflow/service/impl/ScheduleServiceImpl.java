package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.JobOutputJson;
import com.bdilab.dataflow.dto.Metadata;
import com.bdilab.dataflow.dto.MetadataOutputJson;
import com.bdilab.dataflow.dto.OutputData;
import com.bdilab.dataflow.service.ScheduleService;
import com.bdilab.dataflow.service.TableJobService;
import com.bdilab.dataflow.service.TransposeService;
import com.bdilab.dataflow.service.WebSocketServer;
import com.bdilab.dataflow.utils.dag.DagFilterManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.InputDataSlot;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Task scheduling module.
 *
 * @author: zhb
 * @createTime: 2021/11/16 16:15
 */

@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

  @Autowired
  private RealTimeDag realTimeDag;
  @Autowired
  private DagFilterManager dagFilterManager;
  @Autowired
  private TableJobService tableJobService;
  @Autowired
  private TableMetadataServiceImpl tableMetadataService;
  @Autowired
  private TransposeService transposeService;

  @Override
  public void executeTask(String workspaceId, String operatorId) {
    List<String> sortedList = getSortedList(workspaceId, operatorId);

    for (String nodeId : sortedList) {
      DagNode node = realTimeDag.getNode(workspaceId, nodeId);

      Map<Integer, StringBuffer> preFilterMap = new HashMap<>();
      List<Metadata> metadataList = new ArrayList<>();
      // 获取当前结点每个数据源对应的前驱filter id 列表
      Map<Integer, List<String>> filterIdsMap = new HashMap<>();

      InputDataSlot[] inputDataSlots = node.getInputDataSlots();
      for (int i = 0; i < inputDataSlots.length; i++) {
        preFilterMap.put(i, new StringBuffer());
        filterIdsMap.put(i, inputDataSlots[i].getFilterId());
        String dataSource = inputDataSlots[i].getDataSource();
        Metadata metadata = new Metadata(dataSource,
            tableMetadataService.metadataFromDatasource(dataSource));
        metadataList.add(metadata);
      }

      MetadataOutputJson metadataOutputJson = new MetadataOutputJson("JOB_RUNNING", nodeId,
          workspaceId, metadataList);
      WebSocketServer.sendMessage(JSON.toJSONString(metadataOutputJson).toString());

      for (Integer slotNum : filterIdsMap.keySet()) {
        if (!CollectionUtils.isEmpty(filterIdsMap.get(slotNum))) {
          for (String filterId : filterIdsMap.get(slotNum)) {
            preFilterMap.get(slotNum)
                .append(dagFilterManager.getFilter(workspaceId, filterId) + " AND ");
          }
        }
      }

      for (Integer slotNum : preFilterMap.keySet()) {
        preFilterMap.get(slotNum).append(" 1 = 1 ");
      }

      String nodeType = node.getNodeType();
      JobOutputJson outputJson = null;
      switch (nodeType) {
        case "table":
          List<Map<String, Object>> data = tableJobService.saveToClickHouse(node, preFilterMap);

          String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + nodeId;
          OutputData outputData = new OutputData(data,
              tableMetadataService.metadataFromDatasource(tableName));

          outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, outputData);
          break;
        case "filter":
          String filter = preFilterMap.get(0).toString() + " AND " + parseFilterAndPivot(node);
          dagFilterManager.addOrUpdateFilter(workspaceId, nodeId, filter);
          break;
        case "join":
          break;
        case "profiler":
          break;
        case "transpose":
          outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId,
              transposeSavedData(node, nodeId, preFilterMap));
          break;
        case "scalar":
          break;
        default:
          throw new RuntimeException("not exist this operator !");
      }

      if (null != outputJson) {
        WebSocketServer.sendMessage(JSON.toJSONString(outputJson).toString());
      }
    }
  }

  /**
   * Transpose execute: save to clickhouse and return the saved data.
   */
  private OutputData transposeSavedData(DagNode node, String nodeId,
      Map<Integer, StringBuffer> preFilterMap) {
    List<Map<String, Object>> data = tableJobService.saveToClickHouse(node, preFilterMap);
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + nodeId;
    return new OutputData(data, tableMetadataService.metadataFromDatasource(tableName));
  }

  /**
   * Get filter string.
   *
   */
  private String parseFilterAndPivot(DagNode dagNode) {

    JSONObject nodeDescription = JSONObject.parseObject(dagNode.getNodeDescription().toString());
    return nodeDescription.getString("filter");
  }


  /**
   * Gets the traversal order of the graph (topological sorting).
   *
   * @param workspaceId workspace id
   * @param operatorId  operator id
   */
  @Override
  public List<String> getSortedList(String workspaceId, String operatorId) {
    List<String> result = new ArrayList<>();

    Map<String, DagNode> copySubDag = new HashMap<>();
    Queue<String> queue = new LinkedList<>();
    Map<String, List<String>> preNodeIdMap = new HashMap<>();
    Map<String, List<String>> nextNodeIdMap = new HashMap<>();

    DagNode head = realTimeDag.getNode(workspaceId, operatorId);

    queue.offer(operatorId);
    copySubDag.put(operatorId, head);

    // Copy the subGraph (BFS)
    while (!queue.isEmpty()) {
      String curId = queue.poll();

      preNodeIdMap.put(curId, new ArrayList<>());
      nextNodeIdMap.put(curId, new ArrayList<>());

      List<DagNode> nextNodes = realTimeDag.getNextNodes(workspaceId, curId);
      for (DagNode nextNode : nextNodes) {
        if (!copySubDag.containsKey(nextNode.getNodeId())) {
          queue.offer(nextNode.getNodeId());
          copySubDag.put(nextNode.getNodeId(), nextNode);
        }
        nextNodeIdMap.get(curId).add(nextNode.getNodeId());
      }
    }

    // Modify the subgraph structure
    for (String curId : copySubDag.keySet()) {
      List<String> nextNodesId = nextNodeIdMap.get(curId);
      for (String nextNodeId : nextNodesId) {
        preNodeIdMap.get(nextNodeId).add(curId);
      }
    }

    // topological sorting
    Deque<String> stack = new LinkedList<>();
    stack.push(operatorId);
    Set<String> used = new HashSet<>();
    used.add(operatorId);
    while (!stack.isEmpty()) {
      String curId = stack.pop();
      result.add(curId);
      List<String> nextNodesId = nextNodeIdMap.get(curId);
      for (String nextNodeId : nextNodesId) {
        preNodeIdMap.get(nextNodeId).remove(curId);
      }
      // Push nodes with no precursors to the stack
      for (String id : copySubDag.keySet()) {
        if (!used.contains(id) && CollectionUtils.isEmpty(preNodeIdMap.get(id))) {
          stack.push(id);
          used.add(id);
        }
      }
    }

    return result;
  }

}
