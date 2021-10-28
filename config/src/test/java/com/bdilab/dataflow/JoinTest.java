package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.service.JoinService;
import com.bdilab.dataflow.service.impl.TableMetadataServiceImpl;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.bdilab.dataflow.service.impl.JoinServiceImpl;


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

                "\"jobType\": \"join\"," +
                "\"leftDataSource\": \"JoinTest.student\", " +
                "\"rightDataSource\": \"JoinTest.class\", " +
                "\"joinType\": \"left join\", " +
                "\"joinKeys\":[{\"left\": \"classId\",\"right\": \"id\"}]," +
                "\"includePrefixes\": \"true\", " +
                "\"leftPrefix\": \"left_\"," +
                "\"rightPrefix\": \"right_\" " +
                "}";
        JoinDescription joinDescription = JSONObject.parseObject(profilerJson, JoinDescription.class);
        joinService.join(joinDescription);
    }
}