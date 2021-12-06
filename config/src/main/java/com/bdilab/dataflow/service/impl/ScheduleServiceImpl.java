package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.JobOutputJson;
import com.bdilab.dataflow.dto.Metadata;
import com.bdilab.dataflow.dto.MetadataOutputJson;
import com.bdilab.dataflow.dto.OutputData;
import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
import com.bdilab.dataflow.service.*;
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
import org.springframework.util.StringUtils;

/**
 * Task scheduling module.
 *
 * @author zhb
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
  private JoinServiceImpl joinService;
  @Autowired
  private TableMetadataServiceImpl tableMetadataService;
  @Autowired
  private TransposeService transposeService;
  @Autowired
  private ProfilerService profilerService;
  @Autowired
  private ScalarService scalarService;

  @Override
  public void executeTask(String workspaceId, String operatorId) {
    List<String> sortedList = getSortedList(workspaceId, operatorId);
    boolean flag = false;

    for (String nodeId : sortedList) {
      if (flag) break;

      log.info("- Execute the task of the operator with ID [{}] in workspace ID [{}]",
        nodeId, workspaceId);

      DagNode node = realTimeDag.getNode(workspaceId, nodeId);
      String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + nodeId;

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

      String nodeType = node.getNodeType();
      MetadataOutputJson metadataOutputJson = new MetadataOutputJson("JOB_START", nodeId,
        workspaceId, nodeType, metadataList);
      WebSocketServer.sendMessage(JSON.toJSONString(metadataOutputJson));

      for (Integer slotNum : filterIdsMap.keySet()) {
        if (!CollectionUtils.isEmpty(filterIdsMap.get(slotNum))) {
          for (String filterId : filterIdsMap.get(slotNum)) {
            String filter = dagFilterManager.getFilter(workspaceId, filterId);
            if(!StringUtils.isEmpty(filter)){
              preFilterMap.get(slotNum)
                  .append(filter).append(" AND ");
            }
          }
        }
      }

      for (Integer slotNum : preFilterMap.keySet()) {
        preFilterMap.get(slotNum).append(" 1 = 1 ");
      }


      JobOutputJson outputJson = null;
      try {
        switch (nodeType) {
          case "table":
            outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, nodeType,
              tableSavedData(node, tableName, preFilterMap));
            break;
          case "filter":
            String filter = preFilterMap.get(0).toString() + " AND " + parseFilterAndPivot(node);
            dagFilterManager.addOrUpdateFilter(workspaceId, nodeId, filter);
            outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, nodeType, null);
            break;
          case "join":
            JSONObject nodeDescription = (JSONObject) node.getNodeDescription();
            if (nodeDescription.getJSONArray("joinKeys").size() != 0) {
              try {
                joinService.saveToClickHouse(node, preFilterMap);
                outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, nodeType, null);
              } catch (Exception e) {
                outputJson = new JobOutputJson("JOB_FAILED", nodeId, workspaceId, nodeType, null);
                e.printStackTrace();
              }
            } else {
              outputJson = new JobOutputJson("JOB_FAILED", nodeId, workspaceId, nodeType, null);
            }
            break;
          case "profiler":
            //TODO
            List<Map<String, Object>> profilerData = profilerService.getProfiler(node, preFilterMap);
            OutputData outputData = new OutputData();
            outputData.setData(profilerData);
            outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, nodeType, outputData);
            break;
          case "transpose":
            outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, nodeType,
              transposeSavedData(node, tableName, preFilterMap));
            break;
          case "scalar":
            // TODO
            outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, nodeType,
                scalarSavedData(node, tableName, preFilterMap));
            System.out.println(outputJson);
            break;
          default:
            throw new RuntimeException("not exist this operator !");
        }
      } catch (Exception e) {
        outputJson = new JobOutputJson("JOB_FAILED", nodeId, workspaceId, nodeType, null);
        log.error("ClickHouse error !");
        flag = true;
      }

      if (null != outputJson) {
        WebSocketServer.sendMessage(JSON.toJSONString(outputJson));
      }
    }
  }

  /**
   * Table execute: save to ClickHouse and return the saved data.
   */
  private OutputData tableSavedData(DagNode node, String tableName,
                                    Map<Integer, StringBuffer> preFilterMap) {
    List<Map<String, Object>> data = tableJobService.saveToClickHouse(node, preFilterMap);
    return new OutputData(data, tableMetadataService.metadataFromDatasource(tableName));
  }

  /**
   * Transpose execute: save to ClickHouse and return the saved data.
   */
  private OutputData transposeSavedData(DagNode node, String tableName,
                                        Map<Integer, StringBuffer> preFilterMap) {
    List<Map<String, Object>> data = transposeService.saveToClickHouse(node, preFilterMap);
    return new OutputData(data, tableMetadataService.metadataFromDatasource(tableName));
  }

  private OutputData scalarSavedData(DagNode node, String tableName,
                                        Map<Integer, StringBuffer> preFilterMap) {
    // get operator data
    System.out.println(tableName);
    List<Map<String, Object>> data = scalarService.getScalar(node, preFilterMap);

    OutputData outputData = new OutputData();
    outputData.setData(data);
    return outputData;

    // query metadata
//    try {
//      OutputData outputData = new OutputData(data, tableMetadataService.metadataFromDatasource(tableName));
//      System.out.println(outputData);
//      return outputData;
//    }
//    catch (Exception e) {
//      System.out.println(e.getMessage());
//      return null;
//    }
  }

  /**
   * Get filter string.
   */
  private String parseFilterAndPivot(DagNode dagNode) {
    JSONObject nodeDescription = JSONObject.parseObject(dagNode.getNodeDescription().toString());
    String filter = nodeDescription.getString("filter");
    return StringUtils.isEmpty(filter) ? "1 = 1" : filter;
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

    // Modify the subGraph structure
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
