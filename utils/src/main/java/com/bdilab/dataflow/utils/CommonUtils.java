package com.bdilab.dataflow.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Common Utils.

 * @author wh
 * @version 1.0
 * @date 2021/09/16
 *
 */
public class CommonUtils {
    /**
     * objectToListString.
     */
    public static List<String> objectToListString(Object o) {
        ArrayList<String> l = new ArrayList<>();
        if (o instanceof ArrayList<?>) {
            for (Object s : (List<?>) o) {
                l.add((String) s);
            }
        }
        return l;
    }

    /**
     * jsonArray To JsonObejct.
     */
    public static JSONObject[] jsonArrayToJsonObejct(JSONArray jsonArray) {
        int len = jsonArray.size();
        JSONObject[] jsonObjects = new JSONObject[len];
        for (int i = 0; i < len; i++) {
            jsonObjects[i] = jsonArray.getJSONObject(i);
        }
        return jsonObjects;
    }




}
