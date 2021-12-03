package com.bdilab.dataflow.dto.joboutputjson;

import com.bdilab.dataflow.operator.dto.joboutputjson.AbstractJobOutputJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 15:46
 * @version:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScalarOutputJson extends AbstractJobOutputJson {
  private Object value;
}
