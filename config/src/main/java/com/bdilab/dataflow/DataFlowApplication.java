package com.bdilab.dataflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zunjing chen
 * @version 1.0
 * @date 2021/10/24
 * Application Start Class
 */
@ComponentScan("com.bdilab.dataflow")
@MapperScan("com.bdilab.dataflow.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DataFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataFlowApplication.class, args);
    }
}