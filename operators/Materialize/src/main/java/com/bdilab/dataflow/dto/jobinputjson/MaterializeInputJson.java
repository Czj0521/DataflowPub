package com.bdilab.dataflow.dto.jobinputjson;

import com.bdilab.dataflow.dto.jobdescription.MaterializeDescription;
import com.bdilab.dataflow.operator.dto.jobinputjson.AbstractJobInputJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Materialize Input Json.

 * @author: wh
 * @create: 2021-10-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterializeInputJson extends AbstractJobInputJson {
    private MaterializeDescription materializeDescription;
}
