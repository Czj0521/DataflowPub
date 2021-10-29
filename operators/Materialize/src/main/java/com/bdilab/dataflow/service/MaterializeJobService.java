package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobinputjson.MaterializeInputJson;
import com.bdilab.dataflow.dto.joboutputjson.MaterializeOutputJson;

/**
 * Materialize Job Service.

 * @author: wh
 * @create: 2021-10-27
 */
public interface MaterializeJobService {
    /**
     * Materialize Job.

     * @return MaterializeOutputJson
     */
    MaterializeOutputJson materialize(MaterializeInputJson materializeInputJson);
}
