package com.bdilab.dataflow.service;


import com.bdilab.dataflow.utils.dag.DagNode;

import java.util.List;
import java.util.Map;

/**
 * Profiler operator service.

 * @author YuShaochao
 * @create 2021-11-11
 */

public interface ProfilerService{
  List<Map<String, Object>> getProfiler(DagNode dagNode);
}
