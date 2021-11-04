package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobdescription.FilterDescription;

/**
 * Filter Job Service.
 *
 * @author: wh
 * @create: 2021-11-03
 */
public interface FilterJobService {
  /**
   * generate dataSource Sql from filter.
   */
  String generateDataSourceSql(FilterDescription filterDescription);
}
