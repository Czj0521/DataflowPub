package com.bdilab.dataflow.service;

/**
 * Filter Job Service.
 *
 * @author: wh
 * @create: 2021-11-03
 */
import com.bdilab.dataflow.dto.jobdescription.FilterDescription;

public interface FilterJobService {
  /**
   * generate dataSource Sql from filter.
   */
  String generateDataSourceSql(FilterDescription filterDescription);
}
