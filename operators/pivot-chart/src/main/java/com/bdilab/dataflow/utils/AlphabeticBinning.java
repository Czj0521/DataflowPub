package com.bdilab.dataflow.utils;

import java.util.List;

/**
 * @author : [zhangpeiliang]
 * @description : [Alpha分箱，求字符串从第一个字符开始相同字符的长度+1]
 */
public class AlphabeticBinning {
    public static int  commonIndex(List<String> list) {
        //标志位，出现不一样字符，立即终止循环
        boolean flag = true;
        //看门狗标记位，标记字符不一样的位置
        int watchDog = 0;
        //选择第一个字符串与其他字符串按位比较
        String one = list.get(0);
        for (int i = 0; i < one.length() ; i++) {
            if (!flag) {
                break;
            }
            for (int j = 1; j < list.size(); j++) {
                if (list.get(j).charAt(i) != one.charAt(i)) {
                    flag = false;
                    watchDog = i;
                    break;
                }
            }
        }
       return watchDog + 1;
    }
}
