package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.bdilab.dataflow.service.impl.JoinServiceImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JoinTest {
    @Autowired
    JoinServiceImpl joinService;
    @Autowired
    ClickHouseJdbcUtils clickHouseJdbcUtils;


    @ParameterizedTest
    @MethodSource("aggregateProvider")
    public void testJoin(HashMap<String,Object> map) {
        JoinDescription joinDescription = new JoinDescription();

        //git test
        joinDescription.setJobType("join");
        joinDescription.setLeftDataSource((String)map.get("leftDataSource"));
        joinDescription.setRightDataSource((String)map.get("rightDataSource"));
        joinDescription.setJoinType((String)map.get("joinType"));
        joinDescription.setJoinKeys((JSONObject[])map.get("joinKeys"));
        joinDescription.setIncludePrefixes((String)map.get("includePrefixes"));
        joinDescription.setLeftPrefix((String)map.get("leftPrefix"));
        joinDescription.setRightPrefix((String) map.get("rightPrefix"));
        String sql = joinService.join(joinDescription);
        System.out.println(sql);
        sql = sql + " limit 20";
        List<Map<String, Object>> results = clickHouseJdbcUtils.queryForList(sql);
        for(Map<String,Object> result : results) {
            System.out.println(result.values());
        }
    }

    private static List<Arguments> aggregateProvider() {
        return Arrays.<Arguments>asList(
            //user story
            Arguments.arguments(
                new HashMap<String,Object>(){
                    {
                        put("leftDataSource","JoinTest.theme_click_log");
                        put("rightDataSource","JoinTest.user_item_purchase_log");
                        put("joinType","inner join");
                        String keyString =  "[{\"left\": \"user_id\",\"right\": \"user_id\"},{\"left\": \"item_id\",\"right\": \"item_id\"}]";
                        JSONObject[] keySets = JSONObject.parseArray(keyString).toArray(new JSONObject[0]);
                        put("joinKeys",keySets);
                        put("includePrefixes","true");
                        put("leftPrefix","left_");
                        put("rightPrefix","right_");
                    }
                }
            ),
            //inner join test
            Arguments.arguments(
                new HashMap<String,Object>(){
                    {
                        put("leftDataSource","JoinTest.student");
                        put("rightDataSource","JoinTest.class");
                        put("joinType","inner join");
                        String keyString =  "[{\"left\": \"classId\",\"right\": \"id\"}]";
                        JSONObject[] keySets = JSONObject.parseArray(keyString).toArray(new JSONObject[0]);
                        put("joinKeys",keySets);
                        put("includePrefixes","true");
                        put("leftPrefix","left_");
                        put("rightPrefix","right_");
                    }
                }
            ),
            // left join test
            Arguments.arguments(
                new HashMap<String,Object>(){
                    {
                        put("leftDataSource","JoinTest.student");
                        put("rightDataSource","JoinTest.class");
                        put("joinType","left join");
                        String keyString =  "[{\"left\": \"classId\",\"right\": \"id\"}]";
                        JSONObject[] keySets = JSONObject.parseArray(keyString).toArray(new JSONObject[0]);
                        put("joinKeys",keySets);
                        put("includePrefixes","true");
                        put("leftPrefix","left_");
                        put("rightPrefix","right_");
                    }
                }
            ),
            // right join test
            Arguments.arguments(
                new HashMap<String,Object>(){
                    {
                        put("leftDataSource","JoinTest.student");
                        put("rightDataSource","JoinTest.class");
                        put("joinType","right join");
                        String keyString =  "[{\"left\": \"classId\",\"right\": \"id\"}]";
                        JSONObject[] keySets = JSONObject.parseArray(keyString).toArray(new JSONObject[0]);
                        put("joinKeys",keySets);
                        put("includePrefixes","true");
                        put("leftPrefix","left_");
                        put("rightPrefix","right_");
                    }
                }
            ),
            //Prefix test
            Arguments.arguments(
                new HashMap<String,Object>(){
                    {
                        put("leftDataSource","JoinTest.student");
                        put("rightDataSource","JoinTest.class");
                        put("joinType","join");
                        String keyString =  "[{\"left\": \"classId\",\"right\": \"id\"}]";
                        JSONObject[] keySets = JSONObject.parseArray(keyString).toArray(new JSONObject[0]);
                        put("joinKeys",keySets);
                        put("includePrefixes","true");
                        put("leftPrefix","left_");
                        put("rightPrefix","left_");
                    }
                }
            )
        );
    }
}