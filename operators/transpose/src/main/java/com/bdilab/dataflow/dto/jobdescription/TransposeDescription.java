package com.bdilab.dataflow.dto.jobdescription;

import lombok.Data;
/**
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Data
public class TransposeDescription extends JobDescription {
    // 计算列
    String attributes;
    // 横轴
    String column;
    boolean columnIsNumeric;
    // 聚合列 纵轴
    String groupCol;
    // 聚合函数
    String groupFunc;

    public TransposeDescription(String jobType, String dataSource, Integer limit) {
        super(jobType, dataSource, limit);
    }
}
