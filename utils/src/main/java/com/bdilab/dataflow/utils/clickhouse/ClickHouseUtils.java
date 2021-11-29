package com.bdilab.dataflow.utils.clickhouse;

import javax.annotation.Resource;

import com.bdilab.dataflow.common.consts.CommonConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

  /**
   * Copy view or table to new table.
   *
   * @param oldTableName old table or view name
   * @param newTableName new table or view name
   */
  public void copyToTable(String oldTableName, String newTableName) {
    StringBuilder sql = new StringBuilder();
    sql.append("CREATE TABLE ").append(newTableName).append(chooseTableEngine(oldTableName))
        .append(" AS (SELECT * FROM ").append(oldTableName).append(")");
    clickHouseJdbcUtils.execute(new String(sql));
  }

  private String chooseTableEngine(String tableName) {
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
    //todo
    return " ENGINE=Memory ";
  }

  /**
   * delete table with the prefix of 'tempInput_'.
   *
   * @param tableName table name
   */
  public void deleteInputTable(String tableName) {
    if (!StringUtils.isEmpty(tableName) && tableName.startsWith(CommonConstants.TEMP_INPUT_TABLE_PREFIX)) {
      StringBuilder sql = new StringBuilder();
      sql.append("DROP TABLE ").append(tableName);
      clickHouseJdbcUtils.execute(new String(sql));
    }
  }

  /**
   * Delete table or view.
   *
   * @param tableName table or view name
   */
  public void deleteTable(String tableName) {
    StringBuilder sql = new StringBuilder();
    sql.append("DROP TABLE ").append(tableName);
    clickHouseJdbcUtils.execute(new String(sql));
  }

}
