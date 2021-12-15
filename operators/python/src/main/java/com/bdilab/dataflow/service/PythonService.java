package com.bdilab.dataflow.service;

import com.bdilab.dataflow.utils.dag.DagNode;

import java.util.List;
import java.util.Map;

public interface PythonService{
  List<Map<String, Object>> saveToClickHouse(DagNode dagNode);
}
