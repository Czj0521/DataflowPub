package com.bdilab.dataflow.common.enums;

public enum OperatorDataSourceReadyEnum {
  /*操作符是否需要满足所有数据眼*/
  PYTHON("python",false);


  private final String operatorType;
  private final boolean isNeedAllReady;

  OperatorDataSourceReadyEnum(String operatorType,boolean isNeedAllReady) {
    this.operatorType = operatorType;
    this.isNeedAllReady = isNeedAllReady;
  }
  public static boolean isOperatorNeedAllReady(String operatorType){
    return !PYTHON.operatorType.equals(operatorType);
  }

}
