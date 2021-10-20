package com.bdilab.dataflow.dto.pivotchartjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
/**
 * @author wh
 * @version 1.0
 * @date 2021/09/18
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operations {
    private String type;
    private Map<String,String> operation;
}
