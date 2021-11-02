package com.bdilab.dataflow.service.impl;


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
  public List<Map<String, Object>> join(JoinDescription joinDescription) {
    String sql = new JoinSqlGenerator(joinDescription, tableMetadataService).generate();
    System.out.println(sql);
    List<Map<String, Object>> result = clickHouseJdbcUtils.queryForList(sql);
    System.out.println(result);
    return result;
  }

  public String generateDataSourceSql(JoinDescription joinDescription) {
    return new JoinSqlGenerator(joinDescription, tableMetadataService).generateDataSourceSql();
  }
}
