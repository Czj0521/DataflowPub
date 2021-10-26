package com.bdilab.dataflow.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yu Shaochao
 * @create: 2021-10-24
 * @description:
 * "jobDescription": {
 * "inputLeft": "test1",
 *       "inputRight": "test2",
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
    private String inputLeft;
    private String inputRight;
    private String Jointype;
    private JSONObject[] joinKeys;
    private String includePrefixes;
    private String leftPrefix;
    private String rightPrefix;
}