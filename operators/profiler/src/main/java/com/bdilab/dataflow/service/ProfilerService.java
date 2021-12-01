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
@Api(tags = "ProfilerService")
public interface ProfilerService {
  @ApiOperation(value = "getProfiler")
  public List<Map<String, Object>> getProfiler(
      @ApiParam(name = "dagNode", value = "联动节点信息") DagNode dagNode,
      @ApiParam(name = "preFilterMap", value = "filter字符串")  Map<Integer,StringBuffer> preFilterMap);



}
