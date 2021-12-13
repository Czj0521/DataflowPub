package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
import com.bdilab.dataflow.sql.generator.ScalarSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
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

  @Autowired
  private ClickHouseManager clickHouseManager;

  @Deprecated
  // a wrapper method for computing scalar operator in linkage use case.
  public List<Map<String,Object>> getScalar(DagNode node, Map<Integer, StringBuffer> preFilterMap) {
    // get ScalarDescription object
    JSONObject desc = (JSONObject) node.getNodeDescription();
    ScalarDescription scalarDescription = desc.toJavaObject(ScalarDescription.class);

    // handle possible filter
    StringBuffer filter = preFilterMap.get(0);
    if (filter == null || filter.length() == 0) {
      return execute(scalarDescription);
    }
    else {
      String dataSource = "(SELECT * FROM "+ scalarDescription.getDataSource()[0] +" WHERE " + filter +")";
      scalarDescription.setDataSource(new String[] {dataSource});
    }
    return execute(scalarDescription);
  }

  @Override
  public List<Map<String, Object>> execute(ScalarDescription scalarDescription) {
    ScalarSqlGenerator scalarSqlGenerator = new ScalarSqlGenerator(scalarDescription);

    // don't query CH if scalarDescription has no target, aggregation or dataSource.
    if (! scalarDescription.valid()) {
      return null;
    }


    List<Map<String, Object>> res = clickHouseJdbcUtils.queryForList(scalarSqlGenerator.generate());
    if (res.size() == 1) {
      System.out.println(res.get(0).get("scalar"));

      Object value = res.get(0).get("scalar");
      String target = scalarDescription.getTarget();
      String agg = scalarDescription.getAggregation();
      String key = MessageFormat.format("{0} {1}", target, agg);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put(key, value);
      res.set(0, map);

      return res;
    }
    // return null if there are more than one records
    log.warn(MessageFormat.format("Get many records from this aggregation: {0}", scalarDescription));
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode) {
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    ScalarDescription scalarDescription = nodeDescription.toJavaObject(ScalarDescription.class);

    // return the result
    return execute(scalarDescription);
  }
}
