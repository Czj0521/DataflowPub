package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.utils.dag.DagNode;

import java.util.List;
import java.util.Map;

/**
 * join service.
 *
 * @author wjh
 */
public interface JoinService extends OperatorService<JoinDescription>{

  String join(JoinDescription joinDescription);
}
