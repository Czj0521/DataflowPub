package com.bdilab.dataflow.dto.jobinputjson;

import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
import com.bdilab.dataflow.operator.dto.jobinputjson.AbstractJobInputJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 15:40
 * @version:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScalarInputJson extends AbstractJobInputJson {
  private ScalarDescription scalarDescription;
}
