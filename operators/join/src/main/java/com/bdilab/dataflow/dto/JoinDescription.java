package com.bdilab.dataflow.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @author Yu Shaochao
 * @create: 2021-10-24
 * @description:
 * "jobDescription": {
 *       "leftDataSource": "test1",
 *       "rightDataSource": "test2",
 *       "joinType":"innerJoin",
 *       "joinKeys":[{"left":"id","right":"id"},{"left":"id2","right":"id2"}],
 *       "includePrefixes":"false",
 *       "leftPrefix":"left_",
 *       "rightPrefix":"right_"
 *  }
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinDescription {

    private String jobType;
    private String leftDataSource;
    private String rightDataSource;
    private String joinType;
    private JSONObject[] joinKeys;
    private String includePrefixes;
    private String leftPrefix;
    private String rightPrefix;


    /*联动时删除leftDataSource和rightDataSource字段*/
    public static JoinDescription generateFromJson(JSONObject json){
        String jobType = json.getString("jobType");
        String leftDataSource = json.getString("leftDataSource");
        String rightDataSource = json.getString("rightDataSource");
        String joinType = json.getString("joinType");
        JSONObject[] joinKeys = CommonUtils.jsonArrayToJsonObejct(json.getJSONArray("joinKeys"));
        String includePrefixes = json.getString("includePrefixes");
        String leftPrefix = json.getString("leftPrefix");
        String rightPrefix = json.getString("rightPrefix");
        return new JoinDescription(jobType,leftDataSource,rightDataSource,joinType,joinKeys,includePrefixes,leftPrefix,rightPrefix);
    }

}

