package com.bdilab.dataflow.service;


import com.bdilab.dataflow.utils.dag.DagNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * Profiler operator service.

 * @author YuShaochao
 * @create 2021-11-11
 */

public interface ProfilerService {
  public List<Map<String, Object>> getProfiler(DagNode dagNode, Map<Integer,StringBuffer> preFilterMap);
}
