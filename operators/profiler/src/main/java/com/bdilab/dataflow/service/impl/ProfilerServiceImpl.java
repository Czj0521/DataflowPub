package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.common.enums.DataTypeEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.ProfilerDescription;
import com.bdilab.dataflow.dto.ProfilerOutputJson;
import com.bdilab.dataflow.service.ProfilerService;
import com.bdilab.dataflow.utils.PivotChartJobUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Profiler operator service impl

 * @author YuShaochao
 * @create 2021-11-11
 */
@Service
@Slf4j
public class ProfilerServiceImpl implements ProfilerService {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Autowired
  TableMetadataServiceImpl tableMetadataService;
  @Autowired
  PivotChartJobUtils pivotChartJobUtils;
  public List<Map<String, Object>> getProfiler(ProfilerDescription profilerDescription) {
    String dataSource = profilerDescription.getDataSource();
    List<String> profilerColumnList = profilerDescription.getProfilerColumnList();
    List<Map<String, Object>> columnType = getColumnType(dataSource, profilerColumnList);
    List<Map<String, Object>> columnMaxMin = getColumnInfo(dataSource, columnType);
    List<Map<String, Object>> columnAll = getPixo(dataSource, columnMaxMin);
    return columnAll;

  }
  public List<Map<String, Object>> getColumnType(String datasource,List<String> profilerColumnList){
    Map<String, String> metadata = tableMetadataService.metadata("select * from " + datasource);

    List<Map<String,Object>> columnInfoList = new ArrayList<Map<String, Object>>();
    for(String colmnName: profilerColumnList ){
      Map<String,Object> columnInfo = new HashMap<>();
      columnInfo.put("columnName",colmnName);
      columnInfo.put("columnType",metadata.get(colmnName));
      columnInfoList.add(columnInfo);
    }
    return columnInfoList;
  }



  public List<Map<String, Object>> getColumnInfo(String datasource,List<Map<String, Object>> columnInfoList){
    for(Map<String,Object> columnnInfo:columnInfoList){
      String columnName = (String)columnnInfo.get("columnName");
      String columnType = (String)columnnInfo.get("columnType");
      StringBuilder sql = new StringBuilder("select ");

      String dataType = DataTypeEnum.CLICKHOUSE_DATATYPE_MAP.get(columnType);
      if(dataType.equals("numeric")){
        sql.append("avg("+columnName+"),max("+columnName+"),min("+columnName+"),stddevPop("+columnName+"),");
        String selectSql = sql.substring(0,sql.length()-1)+" from " +datasource;
        List<Map<String, Object>> mapList = clickHouseJdbcUtils.queryForList(selectSql);
        Map<String, Object> map = mapList.get(0);
        String mean = map.get("avg("+columnName+")").toString();
        String max = map.get("max("+columnName+")").toString();
        String min = map.get("min("+columnName+")").toString();
        String std = map.get("stddevPop("+columnName+")").toString();
        int i = columnInfoList.indexOf(columnnInfo);
        columnInfoList.get(i).put("mean",mean);
        columnInfoList.get(i).put("max",max);
        columnInfoList.get(i).put("min",min);
        columnInfoList.get(i).put("std",std);

        String sqlNull ="select * from " +
            "(select count(*) from "+datasource+" ) a0,"+
            "(select count(*) from "+datasource+" where "+columnName +" =null) a1," +
            "(select count(*) from "+datasource+" where "+columnName +" =inf ) a2," +
            "(select count(*) from "+datasource+" where "+columnName +" =-inf ) a3,"+
            "(select count(*) from "+datasource+" where "+columnName +" =0 ) a4";
        List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList(sqlNull);
        Map<String,Object> mapNull = maps.get(0);
        Long countRow = ((BigInteger) mapNull.get("a0.count()")).longValue();
        columnInfoList.get(i).put("null",((BigInteger)mapNull.get("a1.count()")).longValue()/countRow);
        columnInfoList.get(i).put("inf",((BigInteger)mapNull.get("a2.count()")).longValue()/countRow);
        columnInfoList.get(i).put("-inf",((BigInteger)mapNull.get("a3.count()")).longValue()/countRow);
        columnInfoList.get(i).put("zeros",((BigInteger)mapNull.get("a4.count()")).longValue()/countRow);
        columnInfoList.get(i).put("nullVal",mapNull.get("a1.count()"));
        columnInfoList.get(i).put("infVal",mapNull.get("a2.count()"));
        columnInfoList.get(i).put("-infVal",mapNull.get("a3.count()"));
        columnInfoList.get(i).put("zerosVal",mapNull.get("a4.count()"));
      }
      else if(dataType.equals("date")) {
        sql.append("max("+columnName+"),min("+columnName+"),");
        String selectSql = sql.substring(0,sql.length()-1)+" from " +datasource;
        List<Map<String, Object>> mapList = clickHouseJdbcUtils.queryForList(selectSql);
        Map<String, Object> map = mapList.get(0);
        String mean = "null";
        String max = map.get("max("+columnName+")").toString();
        String min = map.get("min("+columnName+")").toString();
        String std = "null";
        int i = columnInfoList.indexOf(columnnInfo);
        columnInfoList.get(i).put("mean",mean);
        columnInfoList.get(i).put("max",max);
        columnInfoList.get(i).put("min",min);
        columnInfoList.get(i).put("std",std);

        String sqlNull ="select * from " +
            "(select count(*) from "+datasource+" ) a0,"+
            "(select count(*) from "+datasource+" where "+columnName +" =null) a1";
        List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList(sqlNull);
        Map<String,Object> mapNull = maps.get(0);
        Long countRow = ((BigInteger) mapNull.get("count()")).longValue();
        columnInfoList.get(i).put("null",((BigInteger)mapNull.get("a1.count()")).longValue()/countRow);
        columnInfoList.get(i).put("inf","0");
        columnInfoList.get(i).put("-inf","0");
        columnInfoList.get(i).put("zeros","0");
        columnInfoList.get(i).put("nullVal",mapNull.get("a1.count()"));
        columnInfoList.get(i).put("infVal","null");
        columnInfoList.get(i).put("-infVal","null");
        columnInfoList.get(i).put("zerosVal","null");
      }
      else if (dataType.equals("string")) {
        sql.append(columnName+",count(*) from "+datasource+" group by "+columnName+" order by count()"+" desc limit 3");
        String selectSql= new String(sql);
        List<Map<String, Object>> mapList = clickHouseJdbcUtils.queryForList(selectSql);
        int i = columnInfoList.indexOf(columnnInfo);
        Map<String,String> topValues = new HashMap<>();
        for(Map<String, Object> map: mapList){
          topValues.put((String) map.get(columnName), ((BigInteger)map.get("count()")).toString());
        }
        columnInfoList.get(i).put("topValues",topValues);
      }
    }
    return columnInfoList;
  }
  public List<Map<String, Object>> getPixo(String datasource,List<Map<String, Object>> columnInfoList) {
    for(Map<String,Object> columnInfo : columnInfoList ){
      Map<String, Object> sqlAndCalibration = pivotChartJobUtils.getSqlAndCalibration(datasource,columnInfo);
      String columnType = (String) columnInfo.get("columnType");
      if(DataTypeEnum.CLICKHOUSE_DATATYPE_MAP.get(columnType).equals("string")){
        String columnName = (String)columnInfo.get("columnName");
        List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList("select "+columnName+",count(*) from "+datasource +" group by "+columnName+" order by count() desc");
        List<String> calibration = new ArrayList<>();
        List<Long> countCalibration = new ArrayList<>();
        //System.out.println(maps);
        for(Map<String, Object> map : maps){
          calibration.add((String)map.get(columnName));
          countCalibration.add(((BigInteger)map.get("count()")).longValue());
        }
        int i = columnInfoList.indexOf(columnInfo);
        columnInfoList.get(i).put("calibration",calibration);
        columnInfoList.get(i).put("countCalibration",countCalibration);
      }
      else{
        List<Long> countCalibration = new ArrayList<>();
        for (String sql: (ArrayList<String>)sqlAndCalibration.get("sql")){
          List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList("select count(*) from "+datasource+" where " + sql);
          long l = ((BigInteger) maps.get(0).get("count()")).longValue();
          countCalibration.add(l);
        }
        int i = columnInfoList.indexOf(columnInfo);
        columnInfoList.get(i).put("calibration",sqlAndCalibration.get("calibration"));
        columnInfoList.get(i).put("countCalibration",countCalibration);
      }
    }
    return columnInfoList;
  }

}
