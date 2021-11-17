package com.bdilab.dataflow;

import com.bdilab.dataflow.service.ScheduleService;
import com.bdilab.dataflow.utils.dag.DagManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @description:
 * @author:zhb
 * @createTime:2021/11/16 21:30
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduleTest {
  @Autowired
  RealTimeDag realTimeDag;
  @Autowired
  DagManager dagManager;
  @Autowired
  ScheduleService scheduleService;

  @Test
  void testTopologicalSorting() {
    String workspaceId = "testSchedule";
    String nodeIdO = "o";
    String nodeIdA = "a";
    String nodeIdB = "b";
    String nodeIdC = "c";
    String nodeIdD = "d";
    String nodeIdE = "e";
    DagNode testNodeO = new DagNode(nodeIdO);
    DagNode testNodeA = new DagNode(nodeIdA);
    DagNode testNodeB = new DagNode(nodeIdB);
    DagNode testNodeC = new DagNode(nodeIdC);
    DagNode testNodeD = new DagNode(nodeIdD);
    DagNode testNodeE = new DagNode(nodeIdE);
    if (dagManager.containsWorkspaceId(workspaceId)) {
      dagManager.deleteDag(workspaceId);
    }

    // add node
    realTimeDag.addNode(workspaceId, testNodeO);
    realTimeDag.addNode(workspaceId, testNodeA);
    realTimeDag.addNode(workspaceId, testNodeB);
    realTimeDag.addNode(workspaceId, testNodeC);
    realTimeDag.addNode(workspaceId, testNodeD);
    realTimeDag.addNode(workspaceId, testNodeE);

    // add edge
    realTimeDag.addEdge(workspaceId, nodeIdO, nodeIdC);
    realTimeDag.addEdge(workspaceId, nodeIdB, nodeIdD);
    realTimeDag.addEdge(workspaceId, nodeIdA, nodeIdB);
    realTimeDag.addEdge(workspaceId, nodeIdA, nodeIdD);
    realTimeDag.addEdge(workspaceId, nodeIdB, nodeIdC);
    realTimeDag.addEdge(workspaceId, nodeIdC, nodeIdE);
    realTimeDag.addEdge(workspaceId, nodeIdD, nodeIdE);

    List<String> sortedList = scheduleService.getSortedList(workspaceId, "a");
    System.out.println(sortedList);
  }
}
