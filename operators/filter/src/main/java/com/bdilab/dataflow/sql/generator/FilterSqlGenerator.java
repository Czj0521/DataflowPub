package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter SQL Generator.

 * @author: wh
 * @create: 2021-10-25
 */
@Data
@NoArgsConstructor
public class FilterSqlGenerator extends SqlGeneratorBase {

  private FilterDescription filterDescription;

  public FilterSqlGenerator(FilterDescription filterDescription) {
    super(filterDescription);
    this.filterDescription = filterDescription;
  }

  @Override
  public String project() {
    return "SELECT * ";
  }

  @Override
  public String filter() {
    String filter = filterDescription.getFilter();
    if (filter == null) {
      return "";
    }
    return " WHERE " + filter;
  }

  @Override
  public String generate() {
    return project() + super.datasource(0) + filter();
  }

  public String generateDataSourceSql() {
    return generate();
  }
}
