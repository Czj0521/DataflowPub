package com.bdilab.dataflow.dto.joboutputjson;

import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.operator.dto.joboutputjson.AbstractJobOutputJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author: wh
 * @create: 2021-10-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableOutputJson extends AbstractJobOutputJson {
    private List<Map<String, Object>> outputs;
}
