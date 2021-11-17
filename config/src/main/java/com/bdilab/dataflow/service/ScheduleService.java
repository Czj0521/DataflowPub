package com.bdilab.dataflow.service;

import java.util.List;

/**
 * Task scheduling module.
 *
 * @author: zhb
 * @createTime: 2021/11/16 15:23
 */
public interface ScheduleService {
  void executeTask(String workspaceId, String operatorId);

  List<String> getSortedList(String workspaceId, String operatorId);
}
