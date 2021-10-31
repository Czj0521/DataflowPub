package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SQLGeneratorBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wh
 * @create: 2021-10-25
 * @description: Filter SQL Generator
 */
@Data
@NoArgsConstructor
public class FilterSQLGenerator extends SQLGeneratorBase{

    private FilterDescription filterDescription;

    public FilterSQLGenerator(FilterDescription filterDescription) {
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
        return project() + super.datasource() + filter();
    }

    public String generateDataSourceSql(){
        return generate();
    }
}
