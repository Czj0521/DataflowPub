package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.JoinDescription;
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


@SpringBootTest
public class JoinTest {
    @Autowired
    JoinServiceImpl joinService;

    @ParameterizedTest
    @MethodSource("aggregateProvider")
    public void testJoin(HashMap<String,Object> map) {
        JoinDescription joinDescription = new JoinDescription();
        joinDescription.setJobType("join");
        joinDescription.setLeftDataSource((String)map.get("leftDataSource"));
        joinDescription.setRightDataSource((String)map.get("rightDataSource"));
        joinDescription.setJoinType((String)map.get("joinType"));
        joinDescription.setJoinKeys((JSONObject[])map.get("joinKeys"));
        joinDescription.setIncludePrefixes((String)map.get("includePrefixes"));
        joinDescription.setLeftPrefix((String)map.get("leftPrefix"));
        joinDescription.setRightPrefix((String) map.get("rightPrefix"));
        joinService.join(joinDescription);
    }

    private static List<Arguments> aggregateProvider() {
        return Arrays.<Arguments>asList(
            //inner join test
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