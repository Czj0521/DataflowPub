package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JobOutputJson;
import com.bdilab.dataflow.service.ScheduleService;
import com.bdilab.dataflow.service.TableJobService;
import com.bdilab.dataflow.service.WebSocketServer;
import com.bdilab.dataflow.utils.dag.DagFilterManager;
import com.bdilab.dataflow.utils.dag.DagNode;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Task scheduling module.
 *
 * @author: zhb
 * @createTime: 2021/11/16 16:15
 */

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

      StringBuffer preFilter = new StringBuffer();
      List<String> filterIds = node.getFilterId();
      if (!CollectionUtils.isEmpty(filterIds)) {
        for (String filterId : filterIds) {
          preFilter.append(dagFilterManager.getFilter(workspaceId, filterId) + " AND ");
        }
      }

      String nodeType = node.getNodeType();

      switch (nodeType) {
        case "table":
          List<Map<String, Object>> outputs = tableJobService.saveToClickHouse(node, preFilter + " 1 = 1 ");
          JobOutputJson outputJson = new JobOutputJson("JOB_FINISH", nodeId, workspaceId, outputs);
          WebSocketServer.sendMessage(outputJson.toString());
          break;
        case "filter":
          String filter = preFilter.toString() + parseFilterAndPivot(node);
          dagFilterManager.addOrUpdateFilter(workspaceId, nodeId, filter);
          break;
        case "join":
          break;
        case "profiler":
          break;
        case "transpose":
          break;
        default:
          throw new RuntimeException("not exist this operator !");
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

    DagNode head = realTimeDag.getNode(workspaceId, operatorId);

    Queue<String> queue = new LinkedList<>();
    queue.offer(operatorId);
    copySubDag.put(operatorId, head);

    // Copy the subgraph (BFS)
    while (!queue.isEmpty()) {
      String curId = queue.poll();
      List<DagNode> nextNodes = realTimeDag.getNextNodes(workspaceId, curId);
      for (DagNode nextNode : nextNodes) {
        if (!copySubDag.containsKey(nextNode.getNodeId())) {
          queue.offer(nextNode.getNodeId());
          copySubDag.put(nextNode.getNodeId(), nextNode);
        }
      }
    }

    // Modify the subgraph structure
    for (String curId : copySubDag.keySet()) {
      copySubDag.get(curId).setPreNodesId(null);
    }
    for (String curId : copySubDag.keySet()) {
      List<String> nextNodesId = copySubDag.get(curId).getNextNodesId();
      for (String nextNodeId : nextNodesId) {
        List<String> preList = new ArrayList<>();
        List<String> preNodesId = copySubDag.get(nextNodeId).getPreNodesId();
        if (!CollectionUtils.isEmpty(preNodesId)) {
          preList.addAll(preNodesId);
        }
        preList.add(curId);
        copySubDag.get(nextNodeId).setPreNodesId(preList);
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
      List<String> nextNodesId = copySubDag.get(curId).getNextNodesId();
      for (String nextNodeId : nextNodesId) {
        copySubDag.get(nextNodeId).getPreNodesId().remove(curId);
      }
      // Push nodes with no precursors to the stack
      for (String id : copySubDag.keySet()) {
        if (!used.contains(id) && CollectionUtils.isEmpty(copySubDag.get(id).getPreNodesId())) {
          stack.push(id);
          used.add(id);
        }
      }
    }

    return result;
  }

}
