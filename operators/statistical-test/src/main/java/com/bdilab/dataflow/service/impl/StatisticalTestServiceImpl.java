package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.DataTypeEnum;
import com.bdilab.dataflow.dto.StatisticalTestDescription;
import com.bdilab.dataflow.dto.TdisDescription;
import com.bdilab.dataflow.service.StatisticalTestService;
import com.bdilab.dataflow.utils.ChiSquaredUtils;
import com.bdilab.dataflow.utils.TdisUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * statistical test operator service impl.

 * @author YuShaochao
 * @create 2021-12-20
 */
@Service
public class StatisticalTestServiceImpl implements StatisticalTestService {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Autowired
  TableMetadataServiceImpl tableMetadataService;
  @Autowired
  ChiSquaredUtils chiSquaredUtils;
  @Autowired
  TdisUtils tdisUtils;

  @Override
  public double getPValue(DagNode dagNode) {
    //由dagNode拿到profilerDesc
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    StatisticalTestDescription statisticalTestDescription = nodeDescription
            .toJavaObject(StatisticalTestDescription.class);

    String dataSource1 = statisticalTestDescription.getDataSource()[0];
    String dataSource2 = statisticalTestDescription.getDataSource()[1];

    String dataType1 = getColumnType(dataSource1, statisticalTestDescription.getTest());
    String dataType2 = getColumnType(dataSource2, statisticalTestDescription.getControl());
    double pValue = 0;
    switch (statisticalTestDescription.getType()) {
      case "numerical":
        if (dataType1.equals("numeric") && dataType2.equals("nueric")) {
          TdisDescription tdisDescription = getTdisDescription(statisticalTestDescription);
          pValue = tdisUtils.getP(tdisDescription);
          System.out.println(pValue);
        }
        break;
      case "string":
        Map<Integer, List<Double>> chiList = getChiList(statisticalTestDescription);
        pValue = chiSquaredUtils.getP(chiList.get(1), chiList.get(2));
        break;
      default:
        break;
    }
    return pValue;
  }

  /**
   * get columnType .
   *
   * @return columnType ["numeric","string"]
   */
  public String getColumnType(String dataSource, String columnName) {
    Map<String, String> metadata = tableMetadataService.metadata("select * from " + dataSource);
    String columnType = metadata.get(columnName);
    String dataType = DataTypeEnum.CLICKHOUSE_DATATYPE_MAP.get(columnType);
    return dataType;
  }

  /**
   * get t-test required params.
   *
   * @return mean,count,std of two columns
   */
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
    List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList(sql);
    Map<String, Object> mapNull = maps.get(0);
    TdisDescription tdisDescription = new TdisDescription();
    tdisDescription.setMeanX1((Double) mapNull.get("avg(" + columnName1 + ")"));
    tdisDescription.setN1((Integer) mapNull.get("count(" + columnName1 + ")"));
    Double std1 = (Double) mapNull.get("stddevPop(" + columnName1 + ")");
    tdisDescription.setSampleVariance1(Math.pow(std1, 2));

    String s = "";
    if (columnName1.equals(columnName2)) {
      s = "a1.";
    }
    tdisDescription.setMeanX2((Double) mapNull.get(s + "avg(" + columnName2 + ")"));
    tdisDescription.setN2((Integer) mapNull.get(s + "count(" + columnName2 + ")"));
    Double std2 = (Double) mapNull.get(s + "stddevPop(" + columnName2 + ")");
    tdisDescription.setSampleVariance2(Math.pow(std2, 2));
    return tdisDescription;
  }

  /**
   * get ChiSquared-test required param.
   *
   * @return column value,coulumn count
   */
  public Map<Integer, List<Double>> getChiList(
          StatisticalTestDescription statisticalTestDescription) {
    String dataSource1 = statisticalTestDescription.getDataSource()[0];
    String dataSource2 = statisticalTestDescription.getDataSource()[1];
    String columnName1 = statisticalTestDescription.getTest();
    String columnName2 = statisticalTestDescription.getControl();

    StringBuilder sql1 = new StringBuilder("select ");
    sql1.append(columnName1 + ",count(" + columnName1 + ") ");
    String selectSql1 = sql1.substring(0, sql1.length() - 1) + " from " + dataSource1
            + " group by " + columnName1;

    StringBuilder sql2 = new StringBuilder("select ");
    sql2.append(columnName2 + ",count(" + columnName2 + ") ");
    String selectSql2 = sql2.substring(0, sql2.length() - 1) + " from " + dataSource2
            + "group by " + columnName2;


    List<Map<String, Object>> maps1 = clickHouseJdbcUtils.queryForList(selectSql1);
    Map<String, Object> mapList1 = new HashMap<>();
    for (Map<String, Object> m : maps1) {
      mapList1.put((String) m.get(columnName1), m.get("count(" + columnName1 + ")"));
    }


    List<Map<String, Object>> maps2 = clickHouseJdbcUtils.queryForList(selectSql2);
    Map<String, Object> mapList2 = new HashMap<>();
    for (Map<String, Object> m : maps2) {
      mapList2.put((String) m.get(columnName2), m.get("count(" + columnName2 + ")"));
    }


    Set<String> keySet1 = mapList1.keySet();
    Set<String> keySet2 = mapList2.keySet();
    Set<String> keySetSum = new HashSet<>();
    keySetSum.addAll(keySet1);
    keySetSum.addAll(keySet2);

    List<Double> datalist1 = new ArrayList<>();
    List<Double> datalist2 = new ArrayList<>();
    for (String key : keySetSum) {
      if (keySet1.contains(key)) {
        datalist1.add((Double) mapList1.get(key));
      } else {
        datalist1.add(0D);
      }
      if (keySet2.contains(key)) {
        datalist2.add((Double) mapList2.get(key));
      } else {
        datalist2.add(0D);
      }
    }
    Map<Integer, List<Double>> map = new HashMap<>();
    map.put(1, datalist1);
    map.put(2, datalist2);
    return map;
  }
}
