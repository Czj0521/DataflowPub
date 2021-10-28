package com.bdilab.dataflow.dto.joboutputjson;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.MaterializeDescription;
import com.bdilab.dataflow.operator.dto.jobinputjson.AbstractJobInputJson;
import com.bdilab.dataflow.operator.dto.joboutputjson.AbstractJobOutputJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author: wh
 * @create: 2021-10-27
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterializeOutputJson extends AbstractJobOutputJson {
    private String subTableId;
    private Map<String, String> metadata;
}
