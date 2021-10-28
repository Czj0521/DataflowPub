package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobinputjson.MaterializeInputJson;
import com.bdilab.dataflow.dto.joboutputjson.MaterializeOutputJson;

/**
 * @author: wh
 * @create: 2021-10-27
 * @description:
 */
public interface MaterializeJobService {
    /**
     * Materialize Job
     * @param materializeInputJson
     * @return
     */
    MaterializeOutputJson materialize(MaterializeInputJson materializeInputJson);
}
