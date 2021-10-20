package com.bdilab.dataflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author gluttony team
 * @version 1.0
 * @date 2021/08/28
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DataFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataFlowApplication.class, args);
    }

}
