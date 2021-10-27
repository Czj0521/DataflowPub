package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JoinJson;
import com.bdilab.dataflow.service.JoinService;
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
                "\"job\": \"start_job\"," +
                "\"joinDescription\": { " +
                "\"jobType\": \"join\"," +
                "\"leftDataSource\": \"JoinTest.student\", " +
                "\"rightDataSource\": \"JoinTest.class\", " +
                "\"joinType\": \"inner join\", " +
                "\"joinKeys\":[{\"left\": \"classId\",\"right\": \"id\"}]," +
                "\"includePrefixes\": \"false\", " +
                "\"leftPrefix\": \"left_\"," +
                "\"rightPrefix\": \"right_\" " +
                "}," +
                "\"jobId\": \"1212\"," +
                "\"workspaceId\": \"4f5s4f25s4g8z5eg\" " +
                "}";
        JoinJson ps = JSONObject.parseObject(profilerJson, JoinJson.class);
        //joinService.Join(ps);
        joinService.join(ps);
    }
}