package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.TransposeService;
import com.bdilab.dataflow.sql.generator.TransposeSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class includes methods for querying column and transpose operator.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Service
public class TransposeServiceImpl implements TransposeService {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  /**
   * query column's value.
   *
   * @param transposeDescription transpose's dto
   * @return column value list
   */
  private List<String> columnValues(TransposeDescription transposeDescription) {
    String column = transposeDescription.getColumn();
    String datasource = transposeDescription.getDataSource()[0];
    String sql = "SELECT distinct(" + column + ") FROM " + datasource;
    return clickHouseJdbcUtils.queryForStrList(sql);
  }

  @Override
  public String transpose(TransposeDescription transposeDescription) {
    return new TransposeSqlGenerator(transposeDescription, columnValues(transposeDescription))
        .generate();
  }

  public String generateDataSourceSql(TransposeDescription transposeDescription) {
    return new TransposeSqlGenerator(transposeDescription, columnValues(transposeDescription))
        .generateDataSourceSql();
  }
}
