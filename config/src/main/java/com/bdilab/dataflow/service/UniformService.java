package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;

/**
 * UniformService to handle all operator.
 *
 * @author Zunjing Chen
 * @date 2021-11-03
 **/
public interface UniformService {
  Object analyze(JSONObject json);
}
