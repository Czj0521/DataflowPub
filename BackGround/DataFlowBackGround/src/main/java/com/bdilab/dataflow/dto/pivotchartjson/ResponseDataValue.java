package com.bdilab.dataflow.dto.pivotchartjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/22
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDataValue extends AbstractResponseValue {
    private List<Map<String, Object>> data;
    private Integer dataDim;
}
