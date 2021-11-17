package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.ProfilerDescription;
import com.bdilab.dataflow.service.impl.ProfilerServiceImpl;
import com.bdilab.dataflow.utils.PivotChartJobUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileTest {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Autowired
  ProfilerServiceImpl profilerService;
  @Autowired
  PivotChartJobUtils pivotChartJobUtils;
  @Test
  public void test(){
    ProfilerDescription profilerDescription =new ProfilerDescription();
    profilerDescription.setJobType("profiler");
    profilerDescription.setDataSource("dataflow.airuuid");
    profilerDescription.setProfilerColumnList(new ArrayList<String>(Arrays.asList("time","city","AQI")));
    String dataSource = profilerDescription.getDataSource();
    List<String> columnNameList = profilerDescription.getProfilerColumnList();
    List<Map<String, Object>> profiler = profilerService.getProfiler(profilerDescription);
    System.out.println(profiler);
//    List<Map<String, Object>> columnType = profilerService.getColumnType(dataSource, columnNameList);
//    List<Map<String, Object>> columnMaxMin = profilerService.getColumnInfo(dataSource, columnType);
//    for(Map<String,Object> columnInfo : columnMaxMin ){
//      Map<String, Object> sqlAndCalibration = pivotChartJobUtils.getSqlAndCalibration(dataSource,columnInfo);
//      System.out.println(sqlAndCalibration);
//    }
  }
}
