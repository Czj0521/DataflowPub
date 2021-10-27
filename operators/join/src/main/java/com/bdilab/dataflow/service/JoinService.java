package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.JoinDescription;

import java.util.List;
import java.util.Map;

/**
 * @author wjh
 */
public interface JoinService {
    List<Map<String, Object>> join(JoinDescription joinDescription);
}
