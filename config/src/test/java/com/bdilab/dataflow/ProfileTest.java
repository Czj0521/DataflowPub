package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.ProfilerDescription;
import com.bdilab.dataflow.service.ProfilerService;
import com.bdilab.dataflow.service.impl.ProfilerServiceImpl;
import com.bdilab.dataflow.utils.PivotChartJobUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileTest {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Autowired
  ProfilerService profilerService;
  @Autowired
  PivotChartJobUtils pivotChartJobUtils;
  @Test
  public void test(){
    ProfilerDescription profilerDescription =new ProfilerDescription();
    profilerDescription.setJobType("profiler");
    profilerDescription.setDataSource(new String[]{"dataflow.airuuid"});
    profilerDescription.setProfilerColumnList(new ArrayList<String>(Arrays.asList("time","city","AQI")));
    System.out.println(profilerDescription);

    DagNodeInputDto dagNodeInputDto = new DagNodeInputDto();
    dagNodeInputDto.setNodeDescription(JSONObject.toJSON(profilerDescription));

    DagNode dagNode = new DagNode(dagNodeInputDto);
    System.out.println(dagNode);
    List<Map<String, Object>> profiler = profilerService.getProfiler(dagNode);
    System.out.println(profiler);
//    List<Map<String, Object>> columnType = profilerService.getColumnType(dataSource, columnNameList);
//    List<Map<String, Object>> columnMaxMin = profilerService.getColumnInfo(dataSource, columnType);
//    for(Map<String,Object> columnInfo : columnMaxMin ){
//      Map<String, Object> sqlAndCalibration = pivotChartJobUtils.getSqlAndCalibration(dataSource,columnInfo);
//      System.out.println(sqlAndCalibration);
//    }
  }
}
