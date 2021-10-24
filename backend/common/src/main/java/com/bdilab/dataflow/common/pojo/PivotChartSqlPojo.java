package com.bdilab.dataflow.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/18
 *
 */
@Data
@AllArgsConstructor
public class PivotChartSqlPojo {

    private List<String> unionProjection;
    private List<String> projection;
    private List<String> unionFilter;
    private List<String> unionBasicFilter;
    private List<String> filter;
    private List<String> group;
    private List<String> sort;
    private String limit;

    public PivotChartSqlPojo() {
        this.unionProjection = new ArrayList<>();
        this.projection = new ArrayList<>();
        this.unionFilter = new ArrayList<>();
        this.unionBasicFilter = new ArrayList<>();
        this.filter = new ArrayList<>();
        this.group = new ArrayList<>();
        this.sort = new ArrayList<>();
        this.limit = null;
    }

    public String getUnionProjectionSql() {
        StringBuilder sb = new StringBuilder();
        for (String s : unionProjection) {
            sb.append(s).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public String getProjectionSql() {
        StringBuilder sb = new StringBuilder();
        for (String s : projection) {
            sb.append(s).append(",");
        }
        return sb.substring(0,sb.length()-1);
    }

    public String getFilterSql() {
        StringBuilder sb = new StringBuilder();
        for (String s : filter) {
            sb.append("(").append(s).append(") and ");
        }
        return sb.substring(0,sb.length()-5);
    }

    public String getUnionBasicFilterSql() {
        StringBuilder sb = new StringBuilder();
        for (String s : unionBasicFilter) {
            sb.append(" and (").append(s).append(")");
        }
        return new String(sb);
    }

    public String getGroupSql() {
        StringBuilder sb = new StringBuilder();
        for (String s : group) {
            sb.append(s).append(",");
        }
        return sb.substring(0,sb.length()-1);
    }

    public String getSortSql() {
        StringBuilder sb = new StringBuilder();
        for (String s : sort) {
            sb.append(s).append(",");
        }
        return sb.substring(0,sb.length()-1);
    }
}
