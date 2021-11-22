package com.bdilab.dataflow.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.service.JoinService;
import com.bdilab.dataflow.sql.generator.JoinSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * join service implement.

 * @author: Yu Shaochao
 * @create: 2021-10-24
 * @description:
 */

@Service
public class JoinServiceImpl implements JoinService {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Autowired
  TableMetadataServiceImpl tableMetadataService;

  @Override
  public String join(JoinDescription joinDescription) {
    String sql = new JoinSqlGenerator(joinDescription, tableMetadataService).generate();
    return sql;
  }

  public String join2sql(String joinJson){
    JSONObject joinDescription = JSONObject.parseObject(joinJson).getJSONObject("JoinDescription");
    JoinDescription joinDescription1 = JSON.toJavaObject(joinDescription, JoinDescription.class);
    return join(joinDescription1);
  }

  public String generateDataSourceSql(JoinDescription joinDescription) {
    return new JoinSqlGenerator(joinDescription, tableMetadataService).generateDataSourceSql();
  }
}
