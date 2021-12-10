package com.bdilab.dataflow.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.annotation.WebLog;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Log restful method information.
 *
 * @author wh
 * @date 2021/12/09
 */
@Slf4j
@Aspect
@Component//加上注释则关闭
public class WebLogAspect {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  @Pointcut("@annotation(com.bdilab.dataflow.common.annotation.WebLog)")
  public void webLog() {
  }

  /**
   * Around the point cut.
   */
  @Around("webLog()")
  public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    assert attributes != null;
    HttpServletRequest request = attributes.getRequest();

    String methodDescription = getAspectLogDescription(proceedingJoinPoint);
    log.debug("================================================ Start "
        + "================================================");
    log.debug("URL              : {}", request.getRequestURL().toString());
    log.debug("Description      : {}", methodDescription);
    log.debug("HTTP Method      : {}", request.getMethod());
    log.debug("Class Method     : {}.{}",
        proceedingJoinPoint.getSignature().getDeclaringTypeName(),
        proceedingJoinPoint.getSignature().getName());
    log.debug("IP               : {}", request.getRemoteAddr());
    log.debug("Session Id       : {}", request.getSession().getId());
    log.debug("Request Args     : {}", JSONObject.toJSON(proceedingJoinPoint.getArgs()));
    log.debug("Execute method   : ------------");
    long startTime = System.currentTimeMillis();
    Object result = proceedingJoinPoint.proceed();
    log.debug("Finish execution : ------------");
    log.debug("Response Args    : {}", JSONObject.toJSON(result));
    log.debug("Time-Consuming   : {} ms", System.currentTimeMillis() - startTime);
    log.debug("================================================= End "
        + "=================================================" + LINE_SEPARATOR);
    return result;
  }

  /**
   * Get aspect log description.
   */
  public String getAspectLogDescription(JoinPoint joinPoint)
      throws Exception {
    String targetName = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] arguments = joinPoint.getArgs();
    Class targetClass = Class.forName(targetName);
    Method[] methods = targetClass.getMethods();
    StringBuilder description = new StringBuilder("");
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Class[] clazzs = method.getParameterTypes();
        if (clazzs.length == arguments.length) {
          description.append(method.getAnnotation(WebLog.class).description());
          break;
        }
      }
    }
    return description.toString();
  }

}