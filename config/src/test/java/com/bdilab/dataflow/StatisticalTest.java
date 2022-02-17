package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.StatisticalTestDescription;
import com.bdilab.dataflow.service.WebSocketResolveService;
import com.bdilab.dataflow.service.impl.StatisticalTestServiceImpl;
import com.bdilab.dataflow.utils.dag.DagManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = DataFlowApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StatisticalTest {
    @Autowired
    private WebSocketResolveService webSocketResolveService;
    @Autowired
    private DagManager dagManager;

    @Test
    public void linkTest() {
        dagManager.deleteDag("bdilab");
        String addNode1 ="{" +
            "\"job\": \"start_job\", " +
            "\"statisticaltestDescription\": " +
                   "{\"test\":\"Age\", " +
                   "\"control\":\"LeaseLength\", " +
                   "\"type\":\"numerical\", " +
                   "\"dataSource\":[\"dataflow.promotion_csv\",\"dataflow.promotion_csv\"]}," +
            "\"operatorType\": \"statisticaltest\", " +
            "\"dagType\": \"addNode\", " +
            "\"operatorId\": \"statisticalTest_1\", " +
            "\"workspaceId\": \"bdilab\"}";

        String addNode2 ="{" +
                "\"job\": \"start_job\", " +
                "\"statisticaltestDescription\": " +
                     "{\"test\":\"Age\", " +
                     "\"control\":\"LeaseLength\", " +
                     "\"type\":\"categorical\", " +
                     "\"dataSource\":[\"dataflow.temp_1645003349492\",\"dataflow.temp_1645003349492\"]}," +
                "\"operatorType\": \"statisticaltest\", " +
                "\"dagType\": \"updateNode\", " +
                "\"operatorId\": \"1645004302004\", " +
                "\"workspaceId\": \"glxWorkspace\"}";


        String addNode3 ="{" +
                "\"job\": \"start_job\", " +
                "\"statisticaltestDescription\": " +
                     "{\"test\":\"EducationLevel\", " +
                     "\"control\":\"LeaseLength\", " +
                     "\"type\":\"categorical\", " +
                     "\"dataSource\":[\"( select * from dataflow.promotion_csv where Married != 'single') a0\"," +
                                      "\"( select * from dataflow.promotion_csv where Married = 'single') a1\"]}," +
                "\"operatorType\": \"statisticaltest\", " +
                "\"dagType\": \"addNode\", " +
                "\"operatorId\": \"statisticalTest_1\", " +
                "\"workspaceId\": \"bdilab\"}";

        Assertions.assertDoesNotThrow(
                () -> {
                    webSocketResolveService.resolve(addNode1);
                    webSocketResolveService.resolve(addNode2);
                    webSocketResolveService.resolve(addNode3);
                }
        );
    }




}
