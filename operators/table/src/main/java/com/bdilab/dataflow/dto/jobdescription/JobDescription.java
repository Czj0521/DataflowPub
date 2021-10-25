package com.bdilab.dataflow.dto.jobdescription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-24
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDescription {
    // filter;transform;transpose;join;
    String jobType;
    // 表名或者上一个控件生成的sql
    String dataSource;
    // 返回行数
    Integer limit;
}
