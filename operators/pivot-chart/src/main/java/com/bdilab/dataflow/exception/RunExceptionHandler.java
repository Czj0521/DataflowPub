package com.bdilab.dataflow.exception;

import com.bdilab.dataflow.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器.
 * @ author: [zhangpeiliang]
 */
@Slf4j
@RestControllerAdvice
public class RunExceptionHandler {

  /**
   * 处理自定义异常.
   */
  @ExceptionHandler(RunException.class)
  public R handleRunException(RunException e) {
    log.error(e.getMessage(), e);
    return R.error(e.getCode(), e.getMsg());
  }

  /**
   * 处理未知异常.
   */
  @ExceptionHandler(Exception.class)
  public R handleException(Exception e) {
    log.error(e.getMessage(), e);
    return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
  }
}
