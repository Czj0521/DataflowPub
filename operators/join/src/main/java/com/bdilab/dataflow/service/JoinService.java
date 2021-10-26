package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.JoinJson;

import java.util.List;
import java.util.Map;

public interface JoinService {
    public List<Map<String, Object>> join(JoinJson joinJson);
}
