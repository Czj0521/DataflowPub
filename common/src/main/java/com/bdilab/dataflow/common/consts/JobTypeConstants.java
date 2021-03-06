package com.bdilab.dataflow.common.consts;

/**
 * Job Type Constants.

 * @author: Zunjing Chen
 * @create: 2021-10-09
 **/
public interface JobTypeConstants {
  String FILTER_JOB = "filter";
  String TRANSFORM_JOB = "transform";
  String TRANSPOSE_JOB = "transpose";
  String JOIN_JOB = "join";
  String TABLE_JOB = "table";
  String MATERIALIZE_JOB = "materialize";
  String MUTUAL_INFORMATION = "mutualInformation";


  //wjh
  String PROFILER_JOB = "profiler";
}
