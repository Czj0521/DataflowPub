package com.bdilab.dataflow.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/16
 *
 */
public class CommonUtils {
    public static List<String> objectToListString(Object o){
        ArrayList<String> l = new ArrayList<>();
        if (o instanceof ArrayList<?>) {
            for (Object s : (List<?>) o) {
                l.add((String) s);
            }
        }
        return l;
    }


}
