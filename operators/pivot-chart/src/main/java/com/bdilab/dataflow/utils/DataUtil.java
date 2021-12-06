package com.bdilab.dataflow.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author : [zhangpeiliang]
 * @description : [数据处理工具]
 */
public class DataUtil {

    /**
     * 获取最终要封装的值的集合
     */
    public static List<Object> values(List<?> dataList, Set<Object> binningSet) {
        List<Object> binningList = new ArrayList<>(binningSet);

        List<Object> values = new ArrayList<>();
        for (Object o : dataList) {
            List<Object> list = new ArrayList<>();
            Number value = (Number) o;
            int begin = binningList.indexOf(value);
            int end = begin + 1;
            list.add(binningList.get(begin));
            list.add(binningList.get(end));
            values.add(list);
        }
        return values;
    }
}
