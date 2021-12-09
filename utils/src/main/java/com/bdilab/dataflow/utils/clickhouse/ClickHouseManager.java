package com.bdilab.dataflow.utils.clickhouse;

import com.bdilab.dataflow.common.consts.CommonConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Data table manager for clickhouse in  .
 *
 * @author wh
 * @date 2021/11/16
 */
@Slf4j
@Component
public class ClickHouseManager {
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
    String engine = chooseTableEngine(oldTableName);
    sql.append("DROP TABLE IF EXISTS ").append(newTableName).append(";");
    sql.append("CREATE TABLE ").append(newTableName).append(engine)
        .append(" AS (SELECT * FROM ").append(oldTableName).append(");");
    clickHouseJdbcUtils.execute(new String(sql));
    log.debug("-- Copy table {} to {} with [{}].", oldTableName, newTableName, engine);
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


  public void createTable(String tableName, String selectSql) {
  //    chooseTableEngine(oldTableName);
  }

  public void insertToTable(String tableName) {

  }


  /**
   * Create view
   * with CREATE VIEW {viewName} AS ( {selectSql} ).
   *
   * @param viewName view name
   * @param selectSql select sql
   */
  public void createView(String viewName, String selectSql) {
    StringBuilder sql = new StringBuilder();
    sql.append("DROP VIEW IF EXISTS ").append(viewName).append(";");
    sql.append("CREATE VIEW ").append(viewName).append(" AS ")
        .append("(").append(selectSql).append(");");
    clickHouseJdbcUtils.execute(new String(sql));
    log.debug("-- Drop if exit and create view {}.", viewName);

  }

  /**
   * delete table with the prefix of 'tempInput_'.
   *
   * @param tableName table name
   */
  public void deleteInputTable(String tableName) {
    if (!StringUtils.isEmpty(tableName)
        && tableName.split("\\.")[1].startsWith(CommonConstants.TEMP_INPUT_TABLE_PREFIX)) {
      StringBuilder sql = new StringBuilder();
      sql.append("DROP TABLE IF EXISTS ").append(tableName);
      clickHouseJdbcUtils.execute(new String(sql));
      log.debug("-- Delete input table {}.", tableName);
    }
  }

  /**
   * Delete table or view.
   *
   * @param tableName table or view name
   */
  public void deleteTable(String tableName) {
    StringBuilder sql = new StringBuilder();
    sql.append("DROP TABLE IF EXISTS ").append(tableName);
    clickHouseJdbcUtils.execute(new String(sql));
    log.debug("-- Delete table or view {}.", tableName);
  }

  /**
   * Materialize.
   *
   * @param tempName tempName
   * @param materializeName materializeName
   */
  public void createMaterialize(String tempName, String materializeName) {
    copyToTable(tempName, materializeName);
  }

  /**
   * Get meta data.
   *
   * @param tableName tableName
   * @return metadata
   */
  public Map<String, String> getMetadata(String tableName) {
    List<Map<String, Object>> metaDateMap = clickHouseJdbcUtils.queryForList("desc " + tableName);
    Map<String, String> metaDate = new HashMap<>(metaDateMap.size());
    metaDateMap.forEach((map) -> {
      metaDate.put(String.valueOf(map.get("name")), String.valueOf(map.get("type")));
    });
    return metaDate;
  }
}
