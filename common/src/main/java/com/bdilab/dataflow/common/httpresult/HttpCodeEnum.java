package com.bdilab.dataflow.common.httpresult;

/**
 * Http Code Enum.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/12
 */
public enum HttpCodeEnum {

  /**
   * http code.
   */
  OK(200, "OK"),
  FAIL(400, "Bad Request"),
  UNAUTHORIZED(401, "Unauthorized"),
  FORBIDDEN(403, "Forbidden"),
  NOT_FOUND(404, "Not Found"),
  SERVER_ERROR(500, "Internal Server Error");

  private int code;
  private String message;

  HttpCodeEnum(int code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * codeOf.

   * @param code http code
   */
  public static HttpCodeEnum codeOf(int code) {
    for (HttpCodeEnum codeEnum : values()) {
      if (codeEnum.code == code) {
        return codeEnum;
      }
    }
    return null;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

}
