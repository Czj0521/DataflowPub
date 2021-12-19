package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.StatisticalTestDescription;
import com.bdilab.dataflow.service.impl.StatisticalTestServiceImpl;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticalTest {

    @Autowired
    StatisticalTestServiceImpl statisticalTestService;
    @Test
    public void test1(){
        StatisticalTestDescription statisticalTestDescription = new StatisticalTestDescription();
        statisticalTestDescription.setDataSource(new String[]{"dataflow.promotion_csv","dataflow.promotion_csv"});
        statisticalTestDescription.setTest("Age");
        statisticalTestDescription.setControl("LeaseLength");
        statisticalTestDescription.setType("numerical");

        DagNodeInputDto dagNodeInputDto = new DagNodeInputDto();
        dagNodeInputDto.setNodeDescription(JSONObject.toJSON(statisticalTestDescription));

        DagNode dagNode = new DagNode(dagNodeInputDto);
        statisticalTestService.getPValue(dagNode);
    }
}
