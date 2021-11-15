package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SocketController {

  @Autowired
  private WebSocketServer webSocketServer;

  @RequestMapping("/index")
  public String index() {
    return "index";
  }

  @GetMapping("/webSocket")
  public ModelAndView socket() {
    ModelAndView mav=new ModelAndView("/webSocket");
//        mav.addObject("userId", userId);
    return mav;
  }


}