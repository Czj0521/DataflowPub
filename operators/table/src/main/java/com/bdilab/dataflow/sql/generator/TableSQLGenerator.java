package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.utils.SQLParseUtils;
import org.springframework.util.StringUtils;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-18
 * @description: table 任务sql 解析
 **/
public class TableSQLGenerator extends SQLGeneratorBase {
    private TableDescription tableDescription;
    public TableSQLGenerator(TableDescription tableDescription) {
        super(tableDescription);
        this.tableDescription = tableDescription;
    }

    @Override
    public String project() {
        String[] project = tableDescription.getProject();
        return "SELECT " + SQLParseUtils.combineWithSeparator(project, ",");

    }

    @Override
    public String filter() {
        String filter = tableDescription.getFilter();
        if (StringUtils.isEmpty(filter)) {
            return "";
        }
        return " WHERE " + filter;
    }

    @Override
    public String group() {
        String[] groups = tableDescription.getGroup();
        if (groups == null || groups.length == 0) {
            return "";
        }
        return " GROUP BY " + SQLParseUtils.combineWithSeparator(groups, ",");
    }


    @Override
    public String generate() {
        return project() + super.datasource() + filter() + group() + super.limit();
    }


}
