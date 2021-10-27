package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JoinDescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.bdilab.dataflow.service.impl.JoinServiceImpl;

@SpringBootTest
public class JoinTest {
    @Autowired
    JoinServiceImpl joinService;
    @Test
    public void testJoin() {
        String profilerJson = "{" +
                "\"jobType\": \"join\"," +
                "\"leftDataSource\": \"JoinTest.student\", " +
                "\"rightDataSource\": \"JoinTest.class\", " +
                "\"joinType\": \"inner join\", " +
                "\"joinKeys\":[{\"left\": \"classId\",\"right\": \"id\"}]," +
                "\"includePrefixes\": \"false\", " +
                "\"leftPrefix\": \"left_\"," +
                "\"rightPrefix\": \"right_\" " +
                "}";
        JoinDescription joinDescription = JSONObject.parseObject(profilerJson, JoinDescription.class);
        //joinService.Join(ps);
        joinService.join(joinDescription);
    }
}