package com.bdilab.dataflow.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 * WebSocketConfig.
 *
 * @author wjh
 * @date 2021/11/16
 */

@Configuration
public class WebSocketConfig {
  /**
   * Automatically register websocket endpoints declared with the @ serverside annotation.
   */
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}
