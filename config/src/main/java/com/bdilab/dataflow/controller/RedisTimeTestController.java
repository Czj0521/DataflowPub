package com.bdilab.dataflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.utils.dag.DagManager;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import com.bdilab.dataflow.utils.redis.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test.
 *
 * @author wh
 * @date 2021/11/19
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "TestRedis")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/")
public class RedisTimeTestController {
  @Resource
  RedisUtils redisUtils;
  @Resource
  RealTimeDag realTimeDag;
  @Resource
  DagManager dagManager;
  String workspaceId = "testWorkspaceId";
  String nodeId1 = "testNodeId1";
  String nodeId2 = "testNodeId2";
  String nodeId3 = "testNodeId3";
  String nodeId4 = "testNodeId4";
//  DagNode testNode1 = new DagNode(nodeId1);
//  DagNode testNode2 = new DagNode(nodeId2);
//  DagNode testNode3 = new DagNode(nodeId3);
//  DagNode testNode4 = new DagNode(nodeId4);


  @GetMapping("/testRedisTime")
  @ApiOperation(value = "测试redis时间")
  public void testRedisTime() {
    //    long s = System.currentTimeMillis();
    //    Map<Object, Object> key1 = redisUtils.hmget("map1");
    //    long e = System.currentTimeMillis();
    //    System.out.println("get map1 time:" + (e - s));
    //    System.out.println(key1);
  }

  @GetMapping("/compareDagTime")
  @ApiOperation(value = "对比两种dag时间")
  public void compareDagTime() {
    String workspaceId = "testWorkspaceId";
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
    DagNodeInputDto dagNodeInputDto1 = new DagNodeInputDto(nodeId1, "table", JSONObject.parseObject(nd1));
    DagNodeInputDto dagNodeInputDto2 = new DagNodeInputDto(nodeId2, "table", JSONObject.parseObject(nd2));
    DagNodeInputDto dagNodeInputDto3 = new DagNodeInputDto(nodeId3, "table", JSONObject.parseObject(nd3));
    DagNodeInputDto dagNodeInputDto4 = new DagNodeInputDto(nodeId4, "filter", JSONObject.parseObject(nd4));
    DagNodeInputDto dagNodeInputDto5 = new DagNodeInputDto(nodeId5, "filter", JSONObject.parseObject(nd5));
    DagNodeInputDto dagNodeInputDto6 = new DagNodeInputDto(nodeId6, "join", JSONObject.parseObject(nd6));
    long s, e;
    realTimeDag.clearDag(workspaceId);
    System.out.println("------------------------------------------------------------------------");
    s = System.currentTimeMillis();
    realTimeDag.addNode(workspaceId, dagNodeInputDto1);
    e = System.currentTimeMillis();
    System.out.println("Add a node time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addNode(workspaceId, dagNodeInputDto2);
    e = System.currentTimeMillis();
    System.out.println("Add a node time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addNode(workspaceId, dagNodeInputDto3);
    e = System.currentTimeMillis();
    System.out.println("Add a node time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addNode(workspaceId, dagNodeInputDto4);
    e = System.currentTimeMillis();
    System.out.println("Add a node time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addNode(workspaceId, dagNodeInputDto5);
    e = System.currentTimeMillis();
    System.out.println("Add a node time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addNode(workspaceId, dagNodeInputDto6);
    e = System.currentTimeMillis();
    System.out.println("Add a node time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addEdge(workspaceId, nodeId1, nodeId6, 0);
    e = System.currentTimeMillis();
    System.out.println("Add a edge time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addEdge(workspaceId, nodeId2, nodeId6, 1);
    e = System.currentTimeMillis();
    System.out.println("Add a edge time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addEdge(workspaceId, nodeId4, nodeId6, 0);
    e = System.currentTimeMillis();
    System.out.println("Add a edge time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addEdge(workspaceId, nodeId5, nodeId6, 1);
    e = System.currentTimeMillis();
    System.out.println("Add a edge time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.addEdge(workspaceId, nodeId6, nodeId3, 0);
    e = System.currentTimeMillis();
    System.out.println("Add a edge time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.removeEdge(workspaceId, nodeId4, nodeId6, 0);
    e = System.currentTimeMillis();
    System.out.println("Remove a edge time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.removeEdge(workspaceId, nodeId1, nodeId6, 0);
    e = System.currentTimeMillis();
    System.out.println("Remove a edge time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.removeNode(workspaceId, nodeId6);
    e = System.currentTimeMillis();
    System.out.println("Remove a node time:" + ((e - s)));
    s = System.currentTimeMillis();
    realTimeDag.updateNode(workspaceId, nodeId3, JSONObject.parseObject("{\"dataSource\":[\"new_d3\"]}"));
    e = System.currentTimeMillis();
    System.out.println("Update a edge time:" + ((e - s)));
  }

}
