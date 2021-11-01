package com.bdilab.dataflow.dto.jobdescription;

import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.runtime.JSONFunctions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The description field of the Materialize input JSON.
 *
 * @author: wh
 * @create: 2021-10-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterializeDescription {
  private String materializedType;
  private JSONObject materializedOperator;
}
