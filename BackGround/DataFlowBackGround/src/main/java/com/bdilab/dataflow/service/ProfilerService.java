package com.bdilab.dataflow.service;

import java.util.Map;

public interface ProfilerService {
    Map<String,Object> getProfiler(String tableName, String column);
}
