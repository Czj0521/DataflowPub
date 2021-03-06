package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.operator.link.LinkSqlGenerator;
import com.bdilab.dataflow.utils.SqlParseUtils;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * generate SQL.

 * @author: gluttony team
 * @create: 2021-09-18
 */
public class TableSqlGenerator extends SqlGeneratorBase implements LinkSqlGenerator {
  private TableDescription tableDescription;
  String[] project;
  String[] groups;

  public TableSqlGenerator(TableDescription tableDescription) {
    super(tableDescription);
    this.tableDescription = tableDescription;
  }

  @Override
  public String project() {
    project = tableDescription.getProject();
    if (project == null || project.length == 0) {
      return "SELECT * ";
    }
    return "SELECT " + SqlParseUtils.combineWithSeparator(project, ",");

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
    return " GROUP BY " + SqlParseUtils.combineWithSeparator(groups, ",");
  }


  @Override
  public String generate() {
    return generateDataSourceSql() + super.limit();
  }

  @Override
  public String generateDataSourceSql() {
    String projectStr = project();
    String groupStr = group();

    // SQL syntax check
    if (projectStr.contains("*")) {
      projectStr = "SELECT * ";
      groupStr = "";
    }

    if (groups != null && groups.length > 0 && groupStr.length() > 0) {
      StringBuilder sb = new StringBuilder();
      // remove attributes that are not in 'groups' but in 'project'
      Set<String> set = new HashSet<>();
      for (int i = 0; i < this.groups.length; i++) {
        set.add(this.groups[i]);
      }
      for (int i = 0; i < this.project.length; i++) {
        if (set.contains(this.project[i]) || this.project[i].contains("(")) {
          sb.append(this.project[i] + ",");
        }
      }
      projectStr = "SELECT " + sb.substring(0, sb.length() - 1);
    }

    if (groups == null || groups.length == 0) {
      StringBuilder sb = new StringBuilder();
      if (projectStr.contains("(")) {
        for (int i = 0; i < this.project.length; i++) {
          if (this.project[i].contains("(")) {
            sb.append(this.project[i] + ",");
          }
        }
        projectStr = "SELECT " + sb.substring(0, sb.length() - 1);
      }
    }

    return projectStr + super.datasource(0) + filter() + groupStr;
  }
}
