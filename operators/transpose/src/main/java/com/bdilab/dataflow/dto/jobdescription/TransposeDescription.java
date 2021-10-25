package com.bdilab.dataflow.dto.jobdescription;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Data
public class TransposeDescription extends JobDescription {
    @NotEmpty
    String attribute;
    @NotEmpty
    String column;
    @NotNull
    boolean columnIsNumeric;
    @NotEmpty
    String groupCol;
    @NotEmpty
    String groupFunc;

    public TransposeDescription(String jobType, String dataSource, Integer limit) {
        super(jobType, dataSource, limit);
    }
}
