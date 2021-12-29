package com.bdilab.dataflow.exception;

/**
 * 自定义异常.
 * @ author: [zhangpeiliang]
 */
public class RunException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private String msg;
  private int code = 500;

  public RunException(String msg) {
    super(msg);
    this.msg = msg;
  }

  public RunException(String msg, Throwable e) {
    super(msg, e);
    this.msg = msg;
  }

  /**
   * 运行异常构造函数.
   */
  public RunException(String msg, int code) {
    super(msg);
    this.msg = msg;
    this.code = code;
  }

  /**
   * 运行异常构造函数.
   */
  public RunException(String msg, int code, Throwable e) {
    super(msg, e);
    this.msg = msg;
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
