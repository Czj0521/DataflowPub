package com.bdilab.dataflow.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * WebSocketServer.
 *
 * @author wjh
 */
@ServerEndpoint("/webSocket2")
@Service
public class WebSocketServer {

  //private static final AtomicInteger OnlineCount = new AtomicInteger(0);
  // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
  private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();

  /**
   * 建立连接调用的方法
   */
  @OnOpen
  public void onOpen(Session session) {
    sessionSet.add(session);
  }

  /**
   * 连接关闭调用的方法
   */
  @OnClose
  public void onClose(Session session) {
    sessionSet.remove(session);
  }

  /**
   * 收到客户端消息后调用的方法
   * @param message
   * @param session
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    System.out.println("message :" + message);
  }

  /**
   * 出现错误调用的方法
   * @param session
   * @param error
   */
  @OnError
  public void onError(Session session, Throwable error) {
    error.printStackTrace();
  }

  public static void sendMessage(String message) {
    try {
      for (Session session : sessionSet) {
        session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message, session.getId()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}