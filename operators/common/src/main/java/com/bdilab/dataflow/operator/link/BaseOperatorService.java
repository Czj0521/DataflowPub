package com.bdilab.dataflow.operator.link;

/**
 * BaseOperatorService.
 *
 * @author wjh
 *
 */
public interface BaseOperatorService {

  /**
   * Return the view name according to the data source (2021.11.11) (or the version after SQL).
   * operators need to push are required to implement this interface.
   * You can abstract the public interface in the future.
   *
   * @param dataSource not null
   */
  void CreatTableFromDataSource(String[] dataSource);
}
