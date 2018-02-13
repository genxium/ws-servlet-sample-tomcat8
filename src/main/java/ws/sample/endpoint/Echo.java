package ws.sample.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class Echo {
  
  private static final Logger logger = LoggerFactory.getLogger(Echo.class);
  
  @OnOpen
  public void onOpen(Session session) {
    logger.info("{} has opened a connection.", session.getId());
    try {
      session.getBasicRemote().sendText("Connection Established");
    } catch (IOException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }
  
  @OnClose
  public void onClose(Session session) {
    logger.warn("Session {} has ended.", session.getId());
  }
  
  @OnMessage
  public void onMessage(String message, Session session) {
    logger.info("Message from {}: {}", session.getId(), message);
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }
  
  @OnError
  public void onError(Throwable e) {
    logger.error(e.getMessage(), e);
  }
  
}