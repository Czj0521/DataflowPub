package com.bdilab.dataflow.dto.pivotchartjson;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/22
 *
 */
@Data
@AllArgsConstructor
public class ResponseJobInfor {
    private String mark;
    private Integer valueSize;
    private List<AbstractResponseValue> abstractResponseValues;

    public ResponseJobInfor() {
        abstractResponseValues = new ArrayList<>();
    }
}
