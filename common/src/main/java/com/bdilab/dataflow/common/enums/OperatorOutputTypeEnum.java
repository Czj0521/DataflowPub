package com.bdilab.dataflow.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * The output type of operator.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/17
 */
public enum OperatorOutputTypeEnum {
  /**
   * The output type of filter operator and pivot chart operator is filter.
   */
  FILTER("filter", "filter"),
  PIVOTCHART("pivotchart","filter");

  private final String operatorType;
  private final String outputType;

  public static final Map<String, String> OUTPUT_TYPE =  new HashMap<>();

  static {
    for (OperatorOutputTypeEnum value : OperatorOutputTypeEnum.values()) {
      OUTPUT_TYPE.put(value.getOperatorType(), value.getOutputType());
    }
  }

  public static boolean isFilterOutput(String operatorType){
    return FILTER.getOutputType().equals(OUTPUT_TYPE.get(operatorType.toLowerCase().trim()));
  }

  OperatorOutputTypeEnum(String operatorType, String outputType) {
    this.operatorType = operatorType;
    this.outputType = outputType;
  }

  public String getOperatorType() {
    return operatorType;
  }

  public String getOutputType() {
    return outputType;
  }
}
