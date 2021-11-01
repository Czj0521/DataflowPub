package com.bdilab.dataflow.operator.link;

/**
 * Link Sql Generator.
 *
 * @author: wh
 * @create: 2021-10-27
 */
public interface LinkSqlGenerator {
  /**
   * Get SQL without limit.
   */
  String generateDataSourceSql();
}
