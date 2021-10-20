package com.bdilab.dataflow.dto.pivotchartjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/22
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAxisValue extends AbstractResponseValue {
    private List<Object> axisCalibration;
    private List<Object> height;
}
