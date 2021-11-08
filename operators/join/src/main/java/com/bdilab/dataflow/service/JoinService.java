package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.JoinDescription;
import java.util.List;
import java.util.Map;

/**
 * join service.
 *
 * @author wjh
 */
public interface JoinService {
  String join(JoinDescription joinDescription);
}
