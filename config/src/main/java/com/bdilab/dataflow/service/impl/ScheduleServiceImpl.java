package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.service.ScheduleService;
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

  @Override
  public void executeTask(String workspaceId, String operatorId) {
    List<String> sortedList = getSortedList(workspaceId, operatorId);

    for (String nodeId : sortedList) {
      // ToDo 按顺序依次调度
    }
  }

  /**
   * Gets the traversal order of the graph (topological sorting).
   *
   * @param workspaceId workspace id
   * @param operatorId operator id
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
