package com.bdilab.dataflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: Guo Yongqiang
 * @date: 2021/12/19 22:28
 * @version:
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MiTestApplication {
  public static void main(String[] args) {
    SpringApplication.run(MiTestApplication.class, args);
  }
}
