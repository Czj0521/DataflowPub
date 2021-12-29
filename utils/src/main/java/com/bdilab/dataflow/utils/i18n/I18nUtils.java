package com.bdilab.dataflow.utils.i18n;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * I18nUtils for internationalization.
 *
 * @author Zunjing Chen
 * @date 2021-11-20
 **/
@Slf4j
public class I18nUtils {

  public static String getMessage(String code) {
    return getMessage(code, null);
  }

  /**
   * Get msg.
   */
  public static String getMessage(String code, Object[] args) {
    return getMessage(code, args, "");
  }

  /**
   * Get msg.
   */
  public static String getMessage(String code, Object[] args, String defaultMessage) {
    Locale locale = LocaleContextHolder.getLocale();
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setDefaultEncoding("UTF-8");
    //    messageSource.setBasename("i18n/messages");
    messageSource.setBasenames("i18n/messages", "i18n/messages-pivot-chart");
    String content;
    try {
      content = messageSource.getMessage(code, args, locale);
    } catch (Exception e) {
      log.info("Fail to get i18n message： ->", e);
      content = defaultMessage;
    }
    return content;
  }
}
