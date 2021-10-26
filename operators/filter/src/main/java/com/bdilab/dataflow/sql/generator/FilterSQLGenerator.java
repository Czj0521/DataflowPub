package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wh
 * @create: 2021-10-25
 * @description: Filter SQL Generator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterSQLGenerator extends SQLGeneratorBase {

    private FilterDescription filterDescription;


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
        return project() + super.datasource() + filter() + super.limit();
    }

    public String generateDataSourceSql(){
        return project() + super.datasource() + filter();
    }
}
