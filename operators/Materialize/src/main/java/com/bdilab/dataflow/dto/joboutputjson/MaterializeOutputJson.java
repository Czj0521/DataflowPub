package com.bdilab.dataflow.dto.joboutputjson;

import com.bdilab.dataflow.operator.dto.joboutputjson.AbstractJobOutputJson;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Materialize Output Json.
 *
 * @author: wh
 * @create: 2021-10-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterializeOutputJson extends AbstractJobOutputJson {
  private String subTableId;
  private Map<String, String> metadata;
}
