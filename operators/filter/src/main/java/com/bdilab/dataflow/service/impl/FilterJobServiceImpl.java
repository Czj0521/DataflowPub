package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.service.FilterJobService;
import com.bdilab.dataflow.sql.generator.FilterSqlGenerator;
import org.springframework.stereotype.Service;

/**
 * Filter Job ServiceImpl.
 *
 * @author: wh
 * @create: 2021-11-03
 */
@Service
public class FilterJobServiceImpl implements FilterJobService {
  @Override
  public String generateDataSourceSql(FilterDescription filterDescription) {
    return new FilterSqlGenerator(filterDescription).generateDataSourceSql();
  }
}
