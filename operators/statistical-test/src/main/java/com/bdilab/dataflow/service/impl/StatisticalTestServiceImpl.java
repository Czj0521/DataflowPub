package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.DataTypeEnum;
import com.bdilab.dataflow.dto.StatisticalTestDescription;
import com.bdilab.dataflow.dto.TdisDescription;
import com.bdilab.dataflow.utils.ChiSquaredUtils;
import com.bdilab.dataflow.utils.TdisUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticalTestServiceImpl {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Autowired
  TableMetadataServiceImpl tableMetadataService;
  @Autowired
  ChiSquaredUtils chiSquaredUtils;
  @Autowired
  TdisUtils tdisUtils;

  public double getPValue(DagNode dagNode){
    //由dagNode拿到profilerDesc
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    StatisticalTestDescription statisticalTestDescription = nodeDescription.toJavaObject(StatisticalTestDescription.class);

    String dataSource1 = statisticalTestDescription.getDataSource()[0];
    String dataSource2 = statisticalTestDescription.getDataSource()[1];

    String dataType1 = getColumnType(dataSource1, statisticalTestDescription.getTest());
    String dataType2 = getColumnType(dataSource2,statisticalTestDescription.getControl());

    switch (statisticalTestDescription.getType()){
      case "numerical":
        if(dataType1.equals("numeric") && dataType2.equals("nueric")){
          TdisDescription tdisDescription = getTdisDescription(statisticalTestDescription);
          double pValue = tdisUtils.getP(tdisDescription);
          System.out.println(pValue);
        }
        break;
      case "string": break;
    }

    return 1.0;
  }
  public String getColumnType(String dataSource, String columnName){
    Map<String, String> metadata = tableMetadataService.metadata("select * from " + dataSource);
    String columnType = metadata.get(columnName);
    String dataType = DataTypeEnum.CLICKHOUSE_DATATYPE_MAP.get(columnType);
    return dataType;
  }
  public TdisDescription getTdisDescription(StatisticalTestDescription statisticalTestDescription) {
    String dataSource1 = statisticalTestDescription.getDataSource()[0];
    String dataSource2 = statisticalTestDescription.getDataSource()[1];
    String columnName1 = statisticalTestDescription.getTest();
    String columnName2 = statisticalTestDescription.getControl();

    StringBuilder sql1 = new StringBuilder("select ");
    sql1.append("avg(" + columnName1 + "),count(" + columnName1 + "),"
        + "stddevPop(" + columnName1 + "),");
    String selectSql1 = sql1.substring(0, sql1.length() - 1) + " from " + dataSource1;

    StringBuilder sql2 = new StringBuilder("select ");
    sql2.append("avg(" + columnName2 + "),count(" + columnName2 + "),"
        + "stddevPop(" + columnName2 + "),");
    String selectSql2 = sql2.substring(0, sql2.length() - 1) + " from " + dataSource2;

    String sql = "select * from "
        + "(" + selectSql1 + " ) a0,"
        + "(" + selectSql2 + " ) a1";
    System.out.println(sql);
    return new TdisDescription();
  }
}
