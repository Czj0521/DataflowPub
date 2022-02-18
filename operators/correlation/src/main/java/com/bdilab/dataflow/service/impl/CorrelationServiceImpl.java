package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.CorrelationDescription;
import com.bdilab.dataflow.service.CorrelationService;
import com.bdilab.dataflow.utils.Format;
import com.bdilab.dataflow.utils.Kendall;
import com.bdilab.dataflow.utils.Spearman;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bdilab.dataflow.utils.dag.DagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * CorrelationServiceImpl.
 *
 * @author Liu Pan
 * @date 2021-12-23
 **/
@Service
public class CorrelationServiceImpl implements CorrelationService {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  Kendall kendall = new Kendall();

  Spearman spearman = new Spearman();

  Format format = new Format();

  private List<Double> attributeValues(String attribute, String datasource) {
    String sql = "SELECT " + attribute + " FROM " + datasource;
    return clickHouseJdbcUtils.queryForDouList(sql);
  }

  private Double pearson(String target, String feature, String datasource) {
    String sql = "SELECT corr(" + target + ", " + feature + ") From " + datasource;
    return clickHouseJdbcUtils.queryForDouble(sql);
  }

  @Override
  public List<Map<String, Object>> execute(CorrelationDescription correlationDescription) {
    String target = correlationDescription.getTarget(); // 目标属性
    String[] features = correlationDescription.getFeatures(); // 与目标属性进行相关性度量的特定属性
    String method = correlationDescription.getMethod(); // 相关性度量的方法
    String datasource = correlationDescription.getDataSource()[0];
    Double correlation = 0.0; // 相关系数
    String formatCorrelation = null; // 保留三位有效数字的相关系数
    List<Map<String, Object>> result = new ArrayList<>();
    if (StringUtils.isEmpty(target)) {
      for (int i = 0; i < features.length - 1; i++) {
        for (int j = i + 1; j < features.length; j++) {
          List<Double> targetValuesList = attributeValues(features[i], datasource);
          List<Double> featureValuesList = attributeValues(features[j], datasource);
          switch (method) {
            case "pearson":
              correlation = pearson(features[i], features[j], datasource);
              formatCorrelation = format.formatDigit(correlation);
              break;
            case "spearman":
              correlation = spearman.scorr(targetValuesList, featureValuesList);
              formatCorrelation = format.formatDigit(correlation);
              break;
            case "kendall":
              correlation = kendall.kcorr(targetValuesList, featureValuesList);
              formatCorrelation = format.formatDigit(correlation);
              break;
            default:break;
          }
          Map<String, Object> map = new HashMap<>();
          map.put("attribute_0", features[i]);
          map.put("attribute_1", features[j]);
          map.put("correlation", formatCorrelation);
          result.add(map);
        }
      }
    } else {
      for (String feature : features) {
        List<Double> targetValuesList = attributeValues(target, datasource);
        List<Double> featureValuesList = attributeValues(feature, datasource);
        switch (method) {
          case "pearson":
            correlation = pearson(target, feature, datasource);
            formatCorrelation = format.formatDigit(correlation);
            break;
          case "spearman":
            correlation = spearman.scorr(targetValuesList, featureValuesList);
            formatCorrelation = format.formatDigit(correlation);
            break;
          case "kendall":
            correlation = kendall.kcorr(targetValuesList, featureValuesList);
            formatCorrelation = format.formatDigit(correlation);
            break;
          default:
            break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("attribute_0", target);
        map.put("attribute_1", feature);
        map.put("correlation", formatCorrelation);
        result.add(map);
      }
    }
    return result;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Object extendMessage) {
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    CorrelationDescription correlationDescription = nodeDescription.toJavaObject(CorrelationDescription.class);

    return execute(correlationDescription);
  }
}
