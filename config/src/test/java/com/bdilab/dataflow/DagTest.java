package com.bdilab.dataflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.pojo.PivotChartParameterInfo;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseUtils;
import com.bdilab.dataflow.utils.dag.*;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import com.bdilab.dataflow.utils.redis.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
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
  RealTimeDag realTimeDag;
  @Resource
  DagManager dagManager;
  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Resource
  ClickHouseUtils clickHouseUtils;
  @Resource
  DagFilterManager dagFilterManager;

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
  public void testRealTimeDag(){
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
    DagNodeInputDto dagNodeInputDto1 = new DagNodeInputDto(nodeId1, new String[]{"d1"}, "table", JSONObject.parseObject(nd1));
    DagNodeInputDto dagNodeInputDto2 = new DagNodeInputDto(nodeId2, new String[]{"d2"}, "table", JSONObject.parseObject(nd2));
    DagNodeInputDto dagNodeInputDto3 = new DagNodeInputDto(nodeId3, new String[]{"d3"}, "table", JSONObject.parseObject(nd3));
    DagNodeInputDto dagNodeInputDto4 = new DagNodeInputDto(nodeId4, new String[]{"d4"}, "filter", JSONObject.parseObject(nd4));
    DagNodeInputDto dagNodeInputDto5 = new DagNodeInputDto(nodeId5, new String[]{"d5"}, "filter", JSONObject.parseObject(nd5));
    DagNodeInputDto dagNodeInputDto6 = new DagNodeInputDto(nodeId6, new String[]{"",""}, "join", JSONObject.parseObject(nd6));
    realTimeDag.clearDag(workspaceId);
    realTimeDag.addNode(workspaceId, dagNodeInputDto1);
    realTimeDag.addNode(workspaceId, dagNodeInputDto2);
    realTimeDag.addNode(workspaceId, dagNodeInputDto3);
    realTimeDag.addNode(workspaceId, dagNodeInputDto4);
    realTimeDag.addNode(workspaceId, dagNodeInputDto5);
    realTimeDag.addNode(workspaceId, dagNodeInputDto6);
    realTimeDag.addEdge(workspaceId, nodeId1, nodeId6, 0);
    realTimeDag.addEdge(workspaceId, nodeId2, nodeId6, 1);
    realTimeDag.addEdge(workspaceId, nodeId4, nodeId6, 0);
    realTimeDag.addEdge(workspaceId, nodeId5, nodeId6, 1);
    realTimeDag.addEdge(workspaceId, nodeId6, nodeId3, 0);
    realTimeDag.removeEdge(workspaceId, nodeId4, nodeId6, 0);
    realTimeDag.removeEdge(workspaceId, nodeId1, nodeId6, 0);
    realTimeDag.removeNode(workspaceId, nodeId6);
    realTimeDag.updateNode(workspaceId, nodeId3, JSONObject.parseObject("{\"dataSource\":[\"new_d3\"]}"));
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


//    clickHouseUtils.copyTableToTable("dataflow.TEST_airuuid", "dataflow.test");
//    List<Map<String, Object>> desc_airuuid = clickHouseJdbcUtils.queryForList("select name from (desc airuuid)");
//    for (Map<String, Object> stringObjectMap : desc_airuuid) {
//
//    }
//    clickHouseUtils.copyToTable("dataflow.airuuid", "dataflow.TEST_VIEW5");
    dagFilterManager.deleteFilter("stringd12a3d", "wjhtest1");
  }

}
