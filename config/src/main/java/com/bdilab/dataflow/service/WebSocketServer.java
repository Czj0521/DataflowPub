package com.bdilab.dataflow.service;

import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * WebSocketServer.
 *
 * @author wjh
 */
@ServerEndpoint("/webSocket2")
@Service
public class WebSocketServer {

  // private static final AtomicInteger OnlineCount = new AtomicInteger(0);
  private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();

  /**
   * Connection establishment.
   *
   * @param session The current session.
   */
  @OnOpen
  public void onOpen(Session session) {
    session.setMaxIdleTimeout(360000);
    sessionSet.add(session);
  }

  /**
   * Close connection.
   *
   * @param session The current session.
   */
  @OnClose
  public void onClose(Session session) {
    sessionSet.remove(session);
  }

  /**
   * Receive messages.
   *
   * @param message The message to receive.
   * @param session The current session.
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    System.out.println("message :" + message);
  }

  /**
   * Error occurred.
   *
   * @param session The current session.
   * @param error The error.
   */
  @OnError
  public void onError(Session session, Throwable error) {
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