package com.bdilab.dataflow.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marking method for WebLogAspect.
 *
 * @author wh
 * @date 2021/12/09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {
  /**
   * Aspect description.
   */
  String description() default "";
}
