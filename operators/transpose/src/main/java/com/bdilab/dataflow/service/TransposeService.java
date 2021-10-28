package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;

/**
 * @author: Zunjing Chen
 * @create: 2021-10-25
 * @description: transpose operator service
 **/
public interface TransposeService {
    /**
     * @param transposeDescription
     * @return sql
     */
    String transpose(TransposeDescription transposeDescription);
}
