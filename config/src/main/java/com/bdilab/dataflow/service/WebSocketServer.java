package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
@ServerEndpoint("/webSocket")
@Service
@Slf4j
public class WebSocketServer {
  @Autowired
  private WebSocketResolveService socketResolveService;
  private static AtomicInteger onlineCount = new AtomicInteger(0);
  private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();

  /**
   * Connection establishment.
   *
   * @param session The current session.
   */
  @OnOpen
  public void onOpen(Session session) {
    session.setMaxIdleTimeout(3600000);
    sessionSet.add(session);
    onlineCount.incrementAndGet();
    log.info("Session [{}] has connected, and the number of session is [{}].",
        session.getId(), onlineCount);
  }

  /**
   * Close connection.
   *
   * @param session The current session.
   */
  @OnClose
  public void onClose(Session session) {
    sessionSet.remove(session);
    onlineCount.decrementAndGet();
    log.info("Session [{}] has closed, , and the number of session is [{}].",
        session.getId(), onlineCount);
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
      log.info("WebSocket test : " + message);
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
    try {
      for (Session session : sessionSet) {
        session.getBasicRemote().sendText(String.format("%s", message));
      }
      if (message.length() > 300) {
        message = message.substring(0, 300);
      }
      log.info("Send message to all session. The message: {}", message);
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