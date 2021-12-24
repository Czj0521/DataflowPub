package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.DataTypeEnum;
import com.bdilab.dataflow.dto.jobdescription.MutualInformationDescription;
import com.bdilab.dataflow.sql.generator.MutualInformationSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseHttpUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author: Guo Yongqiang
 * @date: 2021/12/19 20:00
 * @version:
 */
@Slf4j
@Service
public class MutualInformationServiceImpl implements MutualInformationService {
  @Autowired
  private ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Value("${clickhouse.http.url}")
  private String clickHouseHttpPrefix;

  @Override
  public List<Map<String, Object>> execute(MutualInformationDescription description) {

    if (description.getDataSource().length < 1 || description.getTarget().isEmpty() ||
    description.getFeatures().length < 1) {
      return null;
    }

    MutualInformationSqlGenerator generator = new MutualInformationSqlGenerator(description);
    List<Map<String, Object>> res = clickHouseJdbcUtils.queryForList(generator.generate());

    List<Map<String, Object>> data;
    try {
      data = normalize(res);
    }
    catch (RuntimeException e) {
      data = new LinkedList<>();
      Map<String, Object> msg = new HashMap<>();
      msg.put("msg", e.getMessage());
      data.add(msg);
    }
    return data;
  }

  @Override
  public List<Map<String, Object>> getMutualInformation(MutualInformationDescription description) {
    return execute(description);
  }

  @Override
  public List<Map<String, Object>> getAccessibleFeatures(String[] dataSource) {

    URLEncoder encoder = new URLEncoder();
    List<Map<String, Object>> features = new LinkedList<>();
    Map<String, Object> map = new HashMap<>();

    for (String ds: dataSource) {
      String query = MessageFormat.format("desc (select * from {0}) FORMAT TSV", ds);
      String url = clickHouseHttpPrefix + encoder.encode(query, Charset.defaultCharset());
      log.debug("Query clickhouse via HTTP: {}", url);

      String resp = ClickHouseHttpUtils.sendGet(url);
      // System.out.println(resp);
      List<String> l = new LinkedList<>();
      for (String f: resp.split("\n")) {
        String[] split = f.split("\t");
        String javaType = DataTypeEnum.CLICKHOUSE_COLUMN_DATATYPE_MAP.get(split[1]);
        if (javaType.equals("Integer") || javaType.equals("String") || javaType.equals("Boolean")) {
          l.add(split[0]);
        }
      }
      map.put(ds, l);
    }
    features.add(map);

    return features;
  }


  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Object extendMessage) {
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    MutualInformationDescription mutualInformationDescription = nodeDescription.toJavaObject(MutualInformationDescription.class);

    // return the result
    return execute(mutualInformationDescription);
  }

  private List<Map<String, Object>> normalize(List<Map<String, Object>> res) {

    List<Map<String, Object>> data = new LinkedList<>();
    for (Map.Entry<String, Object> entry: res.get(0).entrySet()) {
      // clickhouse jdbc represents tuple with String.
      // System.out.println(entry.getValue().getClass());

      String tuple = (String) entry.getValue();
      String[] values = tuple.substring(1, tuple.length() - 1).split(",");
      double v1, v2;
      v1 = Double.parseDouble(values[0]);
      v2 = Double.parseDouble(values[1]);
      if (v2 == 0) {
        throw new RuntimeException("Target variable is constant!");
      }
      Double uc = v1 / v2;
      Map<String, Object> ucMap = new HashMap<>();
      ucMap.put(entry.getKey(), uc);
      data.add(ucMap);
    }
    return data;
  }
}
