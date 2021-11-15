package com.bdilab.dataflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 16:38
 * @version:
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ScalarTestApplication {
  public static void main(String[] args) {
    SpringApplication.run(ScalarTestApplication.class, args);
  }
}
