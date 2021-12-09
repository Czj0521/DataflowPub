package com.bdilab.dataflow.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Log method run time.
 *
 * @author wh
 * @date 2021/12/09
 */
@Slf4j
@Aspect
@Component
public class LogMethodTimeAspect {
  @Pointcut("@annotation(com.bdilab.dataflow.common.annotation.LogMethodTime)")
  public void pointcutName() {
  }

  /**
   * Around the point cut.
   */
  @Around("pointcutName()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    String methodName = pjp.getSignature().getName();
    long start = System.currentTimeMillis();
    Object result = pjp.proceed();
    long end = System.currentTimeMillis();
    log.debug("Method [{}] consumes time: {}ms", methodName, (end - start));
    return result;
  }
}
