package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.bdilab.dataflow.config.GetHttpSessionConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * WebSocketServer.
 *
 * @author wjh
 */
@ServerEndpoint(value = "/webSocket", configurator = GetHttpSessionConfigurator.class)
@Service
@Slf4j
public class WebSocketServer {
  @Autowired
  private WebSocketResolveService socketResolveService;
  private static AtomicInteger onlineCount = new AtomicInteger(0);
  private static ConcurrentHashMap<HttpSession, Session> sessionMap = new ConcurrentHashMap<>();

  /**
   * Connection establishment.
   *
   * @param wsSession The current session.
   */
  @OnOpen
  public void onOpen(Session wsSession, EndpointConfig config) {
    wsSession.setMaxIdleTimeout(3600000);
    HttpSession httpSession =
        ((HttpSession) config.getUserProperties().get(HttpSession.class.getName()));
    sessionMap.put(httpSession, wsSession);
    onlineCount.incrementAndGet();
    log.info("WsSession [{}] has connected, and httpSession id is [{}].", wsSession.getId(), httpSession.getId());
    log.info("The number of websocket connections is {}", onlineCount);
  }

  /**
   * Close connection.
   *
   * @param wsSession The current session.
   */
  @OnClose
  public void onClose(Session wsSession) {
    for (Map.Entry<HttpSession, Session> e : sessionMap.entrySet()) {
      String id = e.getValue().getId();
      if (wsSession.getId().equals(id)) {
        sessionMap.remove(e.getKey());
        break;
      }
    }
    onlineCount.decrementAndGet();
    log.info("WsSession [{}] has closed.", wsSession.getId());
    log.info("The number of websocket connections is {}", onlineCount);
  }

  /**
   * Receive messages.
   *
   * @param message The message to receive.
   * @param session The current session.
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    try {
      JSON.parse(message);
      socketResolveService.resolve(message);
    } catch (JSONException e) {
      log.debug("WebSocket test : " + message);
    }
  }

  /**
   * Error occurred.
   *
   * @param session The current session.
   * @param error   The error.
   */
  @OnError
  public void onError(Session session, Throwable error) {
    log.error("Session [{}] ERROR: ", session);
    error.printStackTrace();
  }

  /**
   * Send to all sessions.
   *
   * @param message The message to send.
   */
  public static void sendMessage(String message) {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    assert attributes != null;
    HttpServletRequest request = attributes.getRequest();
    Session wsSession = sessionMap.get(request.getSession());
    try {
      wsSession.getBasicRemote().sendText(String.format("%s", message));
      if (message.length() > 300) {
        message = message.substring(0,300);
      }
      log.debug("Send to session [{}]. The message: {}", wsSession.getId(), message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Send to one session.
   *
   * @param message The message to send.
   * @param session The session to send.
   */
  public static void sendMessageP2P(String message, Session session) {
    try {
      session.getBasicRemote().sendText(String.format("%s", message));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}