package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.utils.i18n.I18nUtils;

/**
 * @author tiancong
 * @date 2021/11/29 16:16
 */
public class LocalUtil {

    public static String getENVersion(String msg){
        return I18nUtils.getMessage(msg);
    }
}
