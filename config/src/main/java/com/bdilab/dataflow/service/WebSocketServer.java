package com.bdilab.dataflow.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
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
@ServerEndpoint("/webSocket/{sid}")
@Service
public class WebSocketServer {

  private static WebSocketResolveService webSocketResolveService;

  @Autowired
  public void setWebSocketResolveService(WebSocketResolveService webSocketResolveService) {
    WebSocketServer.webSocketResolveService = webSocketResolveService;
  }


  /**
   * Static variable, used to record the current number of online connections.
   * It should be designed to be thread safe.
   */
  private static AtomicInteger onlineNum = new AtomicInteger();

  /**
   * The thread safe set of the concurrent package is used to store the
   * websockets object corresponding to each client.
   */
  private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

  /**
   * send message.
   *
   * @param session session
   * @param message message
   * @throws IOException IOException
   */

  public void sendMessage(Session session, String message) throws IOException {
    if (session != null) {
      synchronized (session) {
        session.getBasicRemote().sendText(message);
      }
    }
  }

  /**
   * Send information to the specified user.
   *
   * @param userName userName
   * @param message message
   */
  public void sendInfo(String userName, String message) {
    Session session = sessionPools.get(userName);
    try {
      sendMessage(session, message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Call successfully after establishing connection.
   *
   * @param session session
   * @param userName userName
   */
  @OnOpen
  public void onOpen(Session session, @PathParam(value = "sid") String userName) {
    sessionPools.put(userName, session);
    addOnlineCount();
    System.out.println(userName + "加入webSocket！当前人数为" + onlineNum);
    try {
      sendMessage(session, "欢迎" + userName + "加入连接！");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Called when the connection is closed.
   *
   * @param userName userName
   */
  @OnClose
  public void onClose(@PathParam(value = "sid") String userName) {
    sessionPools.remove(userName);
    subOnlineCount();
    System.out.println(userName + "断开webSocket连接！当前人数为" + onlineNum);
  }

  /**
   * Client information received.
   *
   * @param message message
   * @throws IOException IOException
   */
  @OnMessage
  public void onMessage(String message) throws IOException {
    System.out.println(message);
    for (Session session : sessionPools.values()) {
      try {
        webSocketResolveService.resolve(message);
        sendMessage(session, message);
      } catch (Exception e) {
        e.printStackTrace();
        continue;
      }
    }
  }

  /**
   * Called on error.
   *
   * @param session session
   * @param throwable throwable
   */
  @OnError
  public void onError(Session session, Throwable throwable) {
    System.out.println("发生错误");
    throwable.printStackTrace();
  }

  public static void addOnlineCount() {
    onlineNum.incrementAndGet();
  }

  public static void subOnlineCount() {
    onlineNum.decrementAndGet();
  }

}