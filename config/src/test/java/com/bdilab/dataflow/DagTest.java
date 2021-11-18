package com.bdilab.dataflow;

import com.bdilab.dataflow.common.pojo.PivotChartParameterInfo;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.NoRealTimeDag;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import com.bdilab.dataflow.utils.redis.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * UT of the dag.
 *
 * @author: wh
 * @create: 2021-10-30
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DagTest {
  @Resource
  RedisUtils redisUtils;
  @Resource
  NoRealTimeDag noRealTimeDag;
  @Resource
  RealTimeDag realTimeDag;
  @Resource
  DagManager dagManager;
  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Test
  public void testRedisConnect() {
    Map<String, PivotChartParameterInfo> map = new HashMap<>();
    PivotChartParameterInfo pivotChartParameterInfo = new PivotChartParameterInfo();
    pivotChartParameterInfo.setAggregation("sum");
    pivotChartParameterInfo.setAttribute("att");
    map.put("test",pivotChartParameterInfo);
    map.put("test2",pivotChartParameterInfo);
    redisUtils.set("key1",map);
    Map<String, PivotChartParameterInfo> key1 = (HashMap<String, PivotChartParameterInfo>) redisUtils.get("key1");
    System.out.println(key1);
  }
  @Test
  public void testRedis2() {
//    Map<String, Object> map = new HashMap<>();
//    PivotChartParameterInfo pivotChartParameterInfo = new PivotChartParameterInfo();
//    pivotChartParameterInfo.setAggregation("test2");
//    pivotChartParameterInfo.setAttribute("test2");
//    map.put("test2",pivotChartParameterInfo);
//    redisUtils.hmset("map1",map);
    long s = System.currentTimeMillis();
    Map<Object, Object> key1 = redisUtils.hmget("map1");
//    Object key1 = redisUtils.get("key1");
    long e = System.currentTimeMillis();
    System.out.println(e-s);
//    redisUtils.hdel("map1","test24");
    System.out.println(key1);
  }

  @Test
  public void testNoRealTimeDag(){
    String workspaceId = "testWorkspaceId";
    String nodeId1 = "testNodeId1";
    String nodeId2 = "testNodeId2";
    String nodeId3 = "testNodeId3";
    String nodeId4 = "testNodeId4";
    DagNode testNode1 = new DagNode(nodeId1);
    DagNode testNode2 = new DagNode(nodeId2);
    DagNode testNode3 = new DagNode(nodeId3);
    DagNode testNode4 = new DagNode(nodeId4);
    if (dagManager.containsWorkspaceId(workspaceId)) {
      dagManager.deleteDag(workspaceId);
    }
//    noRealTimeDag.loadDagFromRedis(workspaceId);
//    noRealTimeDag.addNode(testNode1);
//    noRealTimeDag.addNode(testNode2);
//    noRealTimeDag.addNode(testNode3);
//    noRealTimeDag.addEdge(nodeId1, nodeId2);
//    noRealTimeDag.addEdge(nodeId2, nodeId3);
//    noRealTimeDag.saveDagToRedis();

    noRealTimeDag.loadDagFromRedis(workspaceId);
    noRealTimeDag.addNode(testNode4);
//    noRealTimeDag.addEdge(nodeId2, nodeId4);
//    noRealTimeDag.removeNode(nodeId2);
    noRealTimeDag.saveDagToRedis();
  }

  @Test
  public void testRealTimeDag(){
    String workspaceId = "testWorkspaceId";
    String nodeId1 = "testNodeId1";
    String nodeId2 = "testNodeId2";
    String nodeId3 = "testNodeId3";
    String nodeId4 = "testNodeId4";
    DagNode testNode1 = new DagNode(nodeId1);
    DagNode testNode2 = new DagNode(nodeId2);
    DagNode testNode3 = new DagNode(nodeId3);
    DagNode testNode4 = new DagNode(nodeId4);
    if (dagManager.containsWorkspaceId(workspaceId)) {
      dagManager.deleteDag(workspaceId);
    }
//    realTimeDag.addNode(workspaceId,testNode1);
//    realTimeDag.addNode(workspaceId,testNode2);
//    realTimeDag.addEdge(workspaceId, nodeId1, nodeId2);
//    realTimeDag.addNode(workspaceId,testNode3);
//    realTimeDag.addNode(workspaceId,testNode4);
//    realTimeDag.addEdge(workspaceId, nodeId2, nodeId3);
//    realTimeDag.addEdge(workspaceId, nodeId2, nodeId4);
//    System.out.println(realTimeDag.getNextNodes(workspaceId, nodeId2));
//    System.out.println(realTimeDag.getPreNodes(workspaceId, nodeId2));
//    System.out.println(realTimeDag.getDag(workspaceId));
//    realTimeDag.removeNode(workspaceId, nodeId2);
//    realTimeDag.addEdge(workspaceId, nodeId1, nodeId3);
//    realTimeDag.addEdge(workspaceId, nodeId1, nodeId4);
//    System.out.println(realTimeDag.getDag(workspaceId));
//    realTimeDag.removeEdge(workspaceId, nodeId1, nodeId3);
//    System.out.println(realTimeDag.getDag(workspaceId));
  }

  @Test
  public void testDagManager(){
    String workspaceId = "testCRUD";
    HashMap<String, Object> testMap = new HashMap<>();
    testMap.put("testKey","testValue");
    redisUtils.hmset(workspaceId, testMap);
    dagManager.deleteDag(workspaceId);
    if (dagManager.containsWorkspaceId(workspaceId)) {
      throw new RuntimeException("DagTest.testDagManager Error !");
    }
  }
  @Test
  public void clickhouseManager(){

//    String sql = "CREATE table TESTVIEW \n" +
//        "ENGINE = MergeTree() \n" +
//        "ORDER BY 三亚_PM2_5_sum\n" +
//        "AS (select * from materialize_e82e5ce9a43a4ef49dd38b3c86cf4db1)";
//    boolean flag = true;
//    while (flag){
//      try {
//        flag = false;
//        clickHouseJdbcUtils.execute(sql);
//      } catch (Exception e){
//        flag = true;
//      }
//    }


  }

}
