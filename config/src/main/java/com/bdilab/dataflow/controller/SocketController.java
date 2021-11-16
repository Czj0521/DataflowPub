package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * this controller is used to open webSocket.
 *
 * @author wjh
 */
@Controller
public class SocketController {

  @Autowired
  private WebSocketServer webSocketServer;

  @GetMapping("/webSocket")
  public ModelAndView socket() {
    ModelAndView mav = new ModelAndView("/webSocket");
    return mav;
  }


}