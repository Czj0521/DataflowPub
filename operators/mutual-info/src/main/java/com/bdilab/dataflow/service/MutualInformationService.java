package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobdescription.MutualInformationDescription;

import java.util.List;
import java.util.Map;

/**
 * @author: Guo Yongqiang
 * @date: 2021/12/19 19:50
 * @version:
 */
public interface MutualInformationService extends OperatorService<MutualInformationDescription> {
  /**
   * Compute Mutual Information values on a dataset.
   * @param description
   * @return
   */
  List<Map<String, Object>> getMutualInformation(MutualInformationDescription description);

  /**
   * Get accessible features of a dataset by Mutual Information operator.
   * @param dataSource
   * @return
   */
  List<Map<String, Object>> getAccessibleFeatures(String[] dataSource);
}
