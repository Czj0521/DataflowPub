package com.bdilab.dataflow;

import com.bdilab.dataflow.common.pojo.PivotChartParameterInfo;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseUtils;
import com.bdilab.dataflow.utils.dag.*;
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
  public void testNoRealTimeDag(){
    String workspaceId = "testWorkspaceId";
    String nodeId1 = "testNodeId1";
    String nodeId2 = "testNodeId2";
    String nodeId3 = "testNodeId3";
    String nodeId4 = "testNodeId4";
  }

  @Test
  public void testRealTimeDag(){
    String workspaceId = "testWorkspaceId";
    String nodeId1 = "testNodeId1";
    String nodeId2 = "testNodeId2";
    String nodeId3 = "testNodeId3";
    String nodeId4 = "testNodeId4";
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
