package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.pivotchartjson.PivotChartResponseJson;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/16
 * PivotChart仪表盘-数据处理Service接口类
 */
public interface PivotChartJobService {
    /**
     *
     * @param inputJson 输入json
     * @return result 输出结果
     * PivotChart仪表盘-数据处理
     */
    PivotChartResponseJson submitPivotChartJob(String inputJson);
}
