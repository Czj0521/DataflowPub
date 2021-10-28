package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JoinJson;
import com.bdilab.dataflow.service.JoinService;
import com.bdilab.dataflow.service.impl.TableMetadataServiceImpl;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.bdilab.dataflow.service.impl.JoinServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class JoinTest {
    @Autowired
    JoinServiceImpl joinService;
    @Autowired
    ClickHouseJdbcUtils clickHouseJdbcUtils;
    @Autowired
    TableMetadataServiceImpl tableMetadataService;
    @Test
    public void testJoin() {
        String profilerJson = "{" +
                "\"job\": \"start_job\"," +
                "\"joinDescription\": { " +
                "\"jobType\": \"join\"," +
                "\"leftDataSource\": \"JoinTest.student\", " +
                "\"rightDataSource\": \"JoinTest.class\", " +
                "\"joinType\": \"left join\", " +
                "\"joinKeys\":[{\"left\": \"classId\",\"right\": \"id\"}]," +
                "\"includePrefixes\": \"true\", " +
                "\"leftPrefix\": \"left_\"," +
                "\"rightPrefix\": \"right_\" " +
                "}," +
                "\"jobId\": \"1212\"," +
                "\"workspaceId\": \"4f5s4f25s4g8z5eg\", " +
                "\"limit\": 2000 "+
                "}";
        JoinJson ps = JSONObject.parseObject(profilerJson, JoinJson.class);
        joinService.join(ps);
    }
}