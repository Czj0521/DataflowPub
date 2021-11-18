package com.bdilab.dataflow.utils.clickhouse;

import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
    StringBuilder sql = new StringBuilder();
    sql.append("CREATE TABLE ").append(newTableName).append(chooseTableEngine(oldTableName))
        .append(" AS (SELECT * FROM ").append(oldTableName).append(")");
    clickHouseJdbcUtils.execute(new String(sql));
  }

  private String chooseTableEngine(String tableName){
    //todo
//    StringBuilder sql = new StringBuilder();
//    sql.append("SELECT count(*) FROM oldTableName");
//    Long size = clickHouseJdbcUtils.queryForLong(new String(sql));
//    StringBuilder ans = new StringBuilder();
//    if(size < 400000){
//      ans.append("ENGINE=Memory");
//    } else {
//      sql = new StringBuilder();
//      sql.append()
//      clickHouseJdbcUtils.queryForList();
//    }
    return " ENGINE=Memory ";
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
