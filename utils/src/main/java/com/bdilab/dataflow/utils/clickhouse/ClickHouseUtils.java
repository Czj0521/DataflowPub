package com.bdilab.dataflow.utils.clickhouse;

import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Data table manager for clickhouse in  .
 *
 * @author wh
 * @date 2021/11/16
 */
@Component
public class ClickHouseUtils {
  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  public void copyToTable(String oldTableName, String newTableName) {
    //todo
//    StringBuilder sql = new StringBuilder();
//    sql.append("rename table  ").append(oldTableName).append("to ").append(newTableName);
//    clickHouseJdbcUtils.execute(new String(sql));
  }

  public void deleteInputTable(String tableName) {
    if (tableName.startsWith("tempInput_")) {
      StringBuilder sql = new StringBuilder();
      sql.append("DROP TABLE ").append(tableName);
      clickHouseJdbcUtils.execute(new String(sql));
    }
  }

  public void deleteTable(String tableName){
    StringBuilder sql = new StringBuilder();
    sql.append("DROP TABLE ").append(tableName);
    clickHouseJdbcUtils.execute(new String(sql));
  }

}
