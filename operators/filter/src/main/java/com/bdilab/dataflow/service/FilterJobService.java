package com.bdilab.dataflow.service;

import java.util.Map;


/**
 * @author: wh
 * @create: 2021-10-25
 * @description: Filter Job Service
 */
public interface FilterJobService {
    /**
     * return all filter information: operator name, data type, sql and support
     * @return All filter information
     */
    Map<String, Map<String, String>> getFilterOperators();
}
