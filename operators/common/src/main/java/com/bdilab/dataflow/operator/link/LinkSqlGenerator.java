package com.bdilab.dataflow.operator.link;

/**
 * @author: wh
 * @create: 2021-10-27
 * @description:
 */
public interface LinkSqlGenerator {
    /**
     * Get SQL without limit.
     * @return
     */
    String generateDataSourceSql();
}
