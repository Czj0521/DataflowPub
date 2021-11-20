package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.common.consts.JobTypeConstants;
import com.bdilab.dataflow.common.consts.OperatorConstants;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseHttpUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Table Metadata ServiceImpl.

 * @author: Zunjing Chen
 * @create: 2021-09-18
 **/
@Service
@Slf4j
public class TableMetadataServiceImpl {
  @Autowired
  private ClickHouseJdbcUtils jdbcUtils;
  @Value("${clickhouse.http.url}")
  private String httpPrefix;


  public Map<String, String> metadataFromDatasource(String datasource) {
    return metadata("SELECT * FROM " + datasource);
  }

  /**
   * Get table names.
   */
  public List<String> getTableName() {
    return jdbcUtils.queryForStrList("show tables from "
      + CommonConstants.DATABASE
      + " not like '"
      + JobTypeConstants.MATERIALIZE_JOB
      + "_%'");
  }

  /**
   * Gets the column name and type of any query statement
   * （This syntax is not supported by JDBC, so use the HTTP interface format）.
   */
  public Map<String, String> metadata(String sql) {
    String query = "desc%20(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")";
    URLEncoder encoder = new URLEncoder();
    sql = encoder.encode(sql, Charset.defaultCharset());
    String url = httpPrefix + query.replace(OperatorConstants.COLUMN_MAGIC_NUMBER, sql);
    Map<String, String> result = new HashMap<>();
    log.info(url);
    String content = ClickHouseHttpUtils.sendGet(url);
    String[] split = content.split("\n");
    for (String s : split) {
      String[] split1 = s.split("\t");
      result.put(split1[0], split1[1]);
    }
    return result;
  }

}
