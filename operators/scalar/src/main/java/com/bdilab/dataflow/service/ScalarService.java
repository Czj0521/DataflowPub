package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
import com.bdilab.dataflow.sql.generator.ScalarSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 15:48
 * @version:
 */

@Slf4j
@Service
public class ScalarService implements OperatorService<ScalarDescription> {

  @Autowired
  private ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Override
  public List<Map<String, Object>> execute(ScalarDescription scalarDescription) {
    ScalarSqlGenerator scalarSqlGenerator = new ScalarSqlGenerator(scalarDescription);
    List<Map<String, Object>> res = clickHouseJdbcUtils.queryForList(scalarSqlGenerator.generate());
    if (res.size() == 1) {
      System.out.println(res.get(0).get("scalar"));

      return res;
    }
    // return null if there are more than one records
    log.warn(MessageFormat.format("Get many records from this aggregation: {0}", scalarDescription));
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Map<Integer, StringBuffer> preFilterMap) {
    return null;
  }
}
