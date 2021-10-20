package com.bdilab.dataflow.flink.uitls;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * @author wh
 * @version 1.0
 * @date 2021/10/15
 *
 */
@Component
@Data
public class FlinkRestConfigUtils {
    @Value("${flink.http.address}")
    private String flinkHttpAddress;
//    @Value("${flink.job.jar.path}")
    @Value("${flink.job.jar.testpath}")
    private String flinkJobJarPath;
    @Value("${flink.job.jar.name}")
    private String flinkJobJarName;
}
