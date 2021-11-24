package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.service.ScheduleService;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
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
  @Autowired
  ClickHouseJdbcUtils jdbcUtils;

  @Test
  void testTopologicalSorting() {
    String workspaceId = "scheduleTest";
    String nodeId1 = "testId1";
    String nodeId2 = "testId2";
    String nodeId3 = "testId3";
    String nodeId4 = "testId4";
    String nodeId5 = "testId5";
    String nodeId6 = "testId6";
    String nd1 = "{\"dataSource\":[\"d1\"]}";
    String nd2 = "{\"dataSource\":[\"d2\"]}";
    String nd3 = "{\"dataSource\":[\"d3\"]}";
    String nd4 = "{\"dataSource\":[\"d4\"]}";
    String nd5 = "{\"dataSource\":[\"d5\"]}";
    String nd6 = "{\"dataSource\":[\"\",\"\"]}";
    DagNodeInputDto dagNodeInputDto1 = new DagNodeInputDto(nodeId1, new String[]{"d1"}, "filter", JSONObject.parseObject(nd1));
    DagNodeInputDto dagNodeInputDto2 = new DagNodeInputDto(nodeId2, new String[]{"d2"}, "filter", JSONObject.parseObject(nd2));
    DagNodeInputDto dagNodeInputDto3 = new DagNodeInputDto(nodeId3, new String[]{"d3"}, "filter", JSONObject.parseObject(nd3));
    DagNodeInputDto dagNodeInputDto4 = new DagNodeInputDto(nodeId4, new String[]{"d4"}, "table", JSONObject.parseObject(nd4));
    DagNodeInputDto dagNodeInputDto5 = new DagNodeInputDto(nodeId5, new String[]{"d5"}, "table", JSONObject.parseObject(nd5));
    DagNodeInputDto dagNodeInputDto6 = new DagNodeInputDto(nodeId6, new String[]{"",""}, "join", JSONObject.parseObject(nd6));
    realTimeDag.clearDag(workspaceId);
    realTimeDag.addNode(workspaceId, dagNodeInputDto1);
    realTimeDag.addNode(workspaceId, dagNodeInputDto2);
    realTimeDag.addNode(workspaceId, dagNodeInputDto3);
    realTimeDag.addNode(workspaceId, dagNodeInputDto4);
    realTimeDag.addNode(workspaceId, dagNodeInputDto5);
    realTimeDag.addNode(workspaceId, dagNodeInputDto6);

    realTimeDag.addEdge(workspaceId, nodeId1, nodeId2, 0);
    realTimeDag.addEdge(workspaceId, nodeId1, nodeId4, 0);
    realTimeDag.addEdge(workspaceId, nodeId2, nodeId3, 0);
    realTimeDag.addEdge(workspaceId, nodeId2, nodeId4, 0);
    realTimeDag.addEdge(workspaceId, nodeId3, nodeId6, 0);
    realTimeDag.addEdge(workspaceId, nodeId3, nodeId5, 0);
    realTimeDag.addEdge(workspaceId, nodeId3, nodeId4, 0);
    realTimeDag.addEdge(workspaceId, nodeId4, nodeId6, 1);

    List<String> sortedList = scheduleService.getSortedList(workspaceId, nodeId1);
    System.out.println(sortedList);
  }
}
