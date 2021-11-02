package com.bdilab.dataflow.dto.jobinputjson;

import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.operator.dto.jobinputjson.AbstractJobInputJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Table InputJson.
 *
 * @author: wh
 * @create: 2021-10-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInputJson extends AbstractJobInputJson {
  private TableDescription tableDescription;
}
