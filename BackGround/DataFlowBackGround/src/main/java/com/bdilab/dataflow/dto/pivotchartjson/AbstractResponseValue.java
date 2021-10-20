package com.bdilab.dataflow.dto.pivotchartjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/22
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponseValue {
    private String type;
}
