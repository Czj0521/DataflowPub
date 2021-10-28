package com.bdilab.dataflow.dto.jobdescription;

import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.runtime.JSONFunctions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author: wh
 * @create: 2021-10-27
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterializeDescription {
    private String MaterializedType;
    private JSONObject materializedOperator;
}
