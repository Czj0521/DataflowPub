package com.bdilab.dataflow.dto.joboutputjson;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数类型响应对象.
 * @ author: [zhangpeiliang]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamTypeRespObj {

  private List<String> string;

  private List<String> booleans;

  private List<String> date;

  private List<String> numeric;
}
