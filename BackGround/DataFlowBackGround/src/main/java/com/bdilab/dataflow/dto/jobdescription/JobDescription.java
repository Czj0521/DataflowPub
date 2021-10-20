package com.bdilab.dataflow.dto.jobdescription;

import lombok.Data;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-24
 * @description:
 **/
@Data
public class JobDescription {
    // filter;transform;transpose;join;
    String jobType;
    // 表名或者上一个控件生成的sql
    String dataSource;
    // 返回行数
    Integer limit;

    public JobDescription() {
    }

    public JobDescription(String jobType, String dataSource, Integer limit) {
        this.jobType = jobType;
        this.dataSource = dataSource;
        this.limit = limit;
    }
}
