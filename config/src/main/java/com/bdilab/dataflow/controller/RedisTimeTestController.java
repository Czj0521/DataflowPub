package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.utils.dag.DagManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.NoRealTimeDag;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import com.bdilab.dataflow.utils.redis.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
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
  NoRealTimeDag noRealTimeDag;
  @Resource
  RealTimeDag realTimeDag;
  @Resource
  DagManager dagManager;
  String workspaceId = "testWorkspaceId";
  String nodeId1 = "testNodeId1";
  String nodeId2 = "testNodeId2";
  String nodeId3 = "testNodeId3";
  String nodeId4 = "testNodeId4";
  DagNode testNode1 = new DagNode(nodeId1);
  DagNode testNode2 = new DagNode(nodeId2);
  DagNode testNode3 = new DagNode(nodeId3);
  DagNode testNode4 = new DagNode(nodeId4);


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
    //    if (dagManager.containsWorkspaceId(workspaceId)) {
    //      dagManager.deleteDag(workspaceId);
    //    }
    //    long s = System.currentTimeMillis();
    //    noRealTimeDag.loadDagFromRedis(workspaceId);
    //    noRealTimeDag.addNode(testNode1);
    //    noRealTimeDag.saveDagToRedis();
    //
    //    noRealTimeDag.loadDagFromRedis(workspaceId);
    //    noRealTimeDag.addNode(testNode2);
    //    noRealTimeDag.saveDagToRedis();
    //
    //    noRealTimeDag.loadDagFromRedis(workspaceId);
    //    noRealTimeDag.addNode(testNode3);
    //    noRealTimeDag.saveDagToRedis();
    //
    //    noRealTimeDag.loadDagFromRedis(workspaceId);
    //    noRealTimeDag.addNode(testNode4);
    //    noRealTimeDag.saveDagToRedis();
    //    long e = System.currentTimeMillis();
    //    System.out.println("Add a node - noRealTimeDag avg time:" +((e - s)/4));
    //
    //    s = System.currentTimeMillis();
    //    noRealTimeDag.loadDagFromRedis(workspaceId);
    //    noRealTimeDag.addEdge(testNode1.getNodeId(), testNode2.getNodeId());
    //    noRealTimeDag.saveDagToRedis();
    //    noRealTimeDag.loadDagFromRedis(workspaceId);
    //    noRealTimeDag.addEdge(testNode2.getNodeId(), testNode3.getNodeId());
    //    noRealTimeDag.saveDagToRedis();
    //    noRealTimeDag.loadDagFromRedis(workspaceId);
    //    noRealTimeDag.addEdge(testNode2.getNodeId(), testNode4.getNodeId());
    //    noRealTimeDag.saveDagToRedis();
    //    e = System.currentTimeMillis();
    //    System.out.println("Add a edge - noRealTimeDag avg time:" +((e - s)/3));
    //
    //    s = System.currentTimeMillis();
    //    noRealTimeDag.loadDagFromRedis(workspaceId);
    //    noRealTimeDag.removeNode(testNode2.getNodeId());
    //    noRealTimeDag.saveDagToRedis();
    //    e = System.currentTimeMillis();
    //    System.out.println("Remove a edge  - noRealTimeDag time:" + ((e - s)));
    //    System.out.println("-----------------------------------------------");
    //
    //
    //    if (dagManager.containsWorkspaceId(workspaceId)) {
    //      dagManager.deleteDag(workspaceId);
    //    }
    //    s = System.currentTimeMillis();
    //    realTimeDag.addNode(workspaceId, testNode1);
    //    realTimeDag.addNode(workspaceId, testNode2);
    //    realTimeDag.addNode(workspaceId, testNode3);
    //    realTimeDag.addNode(workspaceId, testNode4);
    //    e = System.currentTimeMillis();
    //    System.out.println("Add a node - realTimeDag avg time:" + ((e - s)/4));
    //
    //    s = System.currentTimeMillis();
    //    realTimeDag.addEdge(workspaceId, nodeId1, nodeId2);
    //    realTimeDag.addEdge(workspaceId, nodeId2, nodeId3);
    //    realTimeDag.addEdge(workspaceId, nodeId2, nodeId4);
    //    e = System.currentTimeMillis();
    //    System.out.println("Add a edge  - realTimeDag time:" + ((e - s)/3));
    //
    //    s = System.currentTimeMillis();
    //    List<DagNode> nextNodes = realTimeDag.getNextNodes(workspaceId, nodeId2);
    //    e = System.currentTimeMillis();
    //    System.out.println("Get next nodes  - realTimeDag time:" + ((e - s)));
    //
    //    s = System.currentTimeMillis();
    //    realTimeDag.removeNode(workspaceId, nodeId2);
    //    e = System.currentTimeMillis();
    //    System.out.println("Remove a edge  - realTimeDag time:" + ((e - s)));
    //    System.out.println("-----------------------------------------------");
  }

}
