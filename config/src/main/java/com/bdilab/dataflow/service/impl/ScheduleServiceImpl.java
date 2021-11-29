package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JobOutputJson;
import com.bdilab.dataflow.service.ScheduleService;
import com.bdilab.dataflow.service.TableJobService;
import com.bdilab.dataflow.service.WebSocketServer;
import com.bdilab.dataflow.utils.dag.DagFilterManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.InputDataSlot;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

  @Override
  public void executeTask(String workspaceId, String operatorId) {
    List<String> sortedList = getSortedList(workspaceId, operatorId);

    for (String nodeId : sortedList) {
      DagNode node = realTimeDag.getNode(workspaceId, nodeId);

      Map<Integer, StringBuffer> preFilterMap = new HashMap<>();

      // 获取当前结点每个数据源对应的前驱filter id 列表
      Map<Integer, List<String>> filterIdsMap = new HashMap<>();
      InputDataSlot[] inputDataSlots = node.getInputDataSlots();
      for (int i = 0; i < inputDataSlots.length; i++) {
        preFilterMap.put(i, new StringBuffer());
        filterIdsMap.put(i, inputDataSlots[i].getFilterId());
      }

      for (Integer slotNum : filterIdsMap.keySet()) {
        if (!CollectionUtils.isEmpty(filterIdsMap.get(slotNum))) {
          for (String filterId : filterIdsMap.get(slotNum)) {
            preFilterMap.get(slotNum).append(dagFilterManager.getFilter(workspaceId, filterId) + " AND ");
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
          List<Map<String, Object>> outputs = tableJobService.saveToClickHouse(node, preFilterMap);
          outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, outputs);
          break;
        case "filter":
          String filter = preFilterMap.get(0).toString() + " AND "+ parseFilterAndPivot(node);
          log.info("The current filter :" + filter);
          dagFilterManager.addOrUpdateFilter(workspaceId, nodeId, filter);
          break;
        case "join":
          break;
        case "profiler":
          break;
        case "transpose":
          break;
        case "scalar":
          break;
        default:
          throw new RuntimeException("not exist this operator !");
      }

      if(null != outputJson) {
        WebSocketServer.sendMessage(JSON.toJSONString(outputJson).toString());
      }
    }
  }

  /**
   * Get filter string
   *
   * @param dagNode
   * @return
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
