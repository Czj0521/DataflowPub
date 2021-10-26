package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.utils.SQLParseUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: gluttony team
 * @create: 2021-09-18
 * @description: generate SQL
 **/
public class TableSQLGenerator extends SQLGeneratorBase {
    private TableDescription tableDescription;
    String[] project;
    String[] groups;
    public TableSQLGenerator(TableDescription tableDescription) {
        super(tableDescription);
        this.tableDescription = tableDescription;
    }

    @Override
    public String project() {
        project = tableDescription.getProject();
        if(project == null || project.length == 0) {
            return "SELECT * ";
        }
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
        groups = tableDescription.getGroup();
        if (groups == null || groups.length == 0) {
            return "";
        }
        return " GROUP BY " + SQLParseUtils.combineWithSeparator(groups, ",");
    }


    @Override
    public String generate() {
        String projectStr = project();
        String groupStr = group();

        // SQL syntax check
        if(projectStr.contains("*")) {
            projectStr = "SELECT * ";
            groupStr = "";
        }
        if(groups != null && groups.length > 0 && groupStr.length() > 0) {
            StringBuilder sb = new StringBuilder();
            // remove attributes that are not in 'groups' but in 'project'
            Set<String> set = new HashSet<>();
            for (int i = 0; i < this.groups.length; i++) {
                set.add(this.groups[i]);
            }
            for (int i = 0; i < this.project.length; i++) {
                if(set.contains(this.project[i]) || this.project[i].contains("(")) {
                    sb.append(this.project[i] + ",");
                }
            }
            projectStr = "SELECT " + sb.substring(0, sb.length() - 1);
        }

        return projectStr + super.datasource() + filter() + groupStr + super.limit();
    }


}
