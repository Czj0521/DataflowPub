package com.bdilab.dataflow.utils.sql;

import com.bdilab.dataflow.dto.jobdescription.FilterDescription;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-22
 * @description:
 **/
public class FilterSQLGenerator extends SQLGeneratorBase {

    private FilterDescription filterDescription;
    private String UUID;

    public FilterSQLGenerator(FilterDescription filterDescription, String UUID) {
        super(filterDescription);
        this.filterDescription = filterDescription;
        this.UUID = UUID;
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
        String prefix = "CREATE VIEW dataflow." + UUID + " AS ";
        return prefix + project() + super.datasource() + filter() + super.limit();
    }
}
