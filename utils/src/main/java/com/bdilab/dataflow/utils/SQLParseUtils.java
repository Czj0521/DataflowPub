package com.bdilab.dataflow.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-16
 * @description:
 **/
public class SQLParseUtils {
    /**
     * json 数组字符串转list
     */
    public static List getJsonArrToList(JSONObject json, String filedName, Class clazz) {
        return json.getJSONArray(filedName).toJavaList(clazz);
    }


    /**
     * @param list
     * @param separator
     * @return
     */
    public static String combineWithSeparator(String[] list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    /**
     * 表/视图id
     */
    public static String getUUID32() {

        return UUID.randomUUID().toString().replace("-", "").toLowerCase();

    }

//    /**
//     *
//     * @param column 原始列名
//     * @return 规范化的列名
//     */
//    public static String columnNameNormalize(String column){
//        column.replace("-","_");
//        column.replace("-","_");
//    }

}
