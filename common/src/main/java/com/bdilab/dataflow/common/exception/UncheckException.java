package com.bdilab.dataflow.common.exception;

/**
 * UncheckException.

 * @author: Zunjing Chen
 * @create: 2021-09-17
 **/
public class UncheckException extends RuntimeException {
  Integer code;

  public UncheckException(String msg) {
    super(msg);
  }

  public UncheckException(String msg, Integer code) {
    super(msg);
    this.code = code;
  }
}
