package ws.sample.endpoint;

import org.apache.catalina.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class Echo {
  
  private static final Logger logger = LoggerFactory.getLogger(Echo.class);
  
  private static final Map<String, String> tokenToUidDict = new HashMap<>();
  static {
    tokenToUidDict.put("aaaaaaaaa", "1");
    tokenToUidDict.put("bbbbbbbbb", "2");
    tokenToUidDict.put("ccccccccc", "3");
  }
  
  private Supplier<String> checkToken(final String token) {
    return () -> {
      // A snippet of synchronous task here.
      return tokenToUidDict.getOrDefault(token, null);
    };
  }
  
  @OnOpen
  public void onOpen(Session session) throws IOException {
    final String queryStr = session.getQueryString();
    logger.info("Session.id == {} has opened a connection with queryStr: {}.", session.getId(), queryStr);
  
    final Map<String, String[]> queryParamDict = new HashMap<>();
    RequestUtil.parseParameters(queryParamDict, queryStr, Charset.forName("UTF-8").name());
  
    final String missingTokenHint = "Missing required param \"token\".";
  
    final String TOKEN = "token";
    if (queryParamDict.containsKey(TOKEN)) {
      final String[] tokens = queryParamDict.get(TOKEN);
      if (0 < tokens.length) {
        final String token = tokens[0];
        if (null != token) {
          CompletableFuture<String> uidCompletableFuture = CompletableFuture.supplyAsync(checkToken(token));
          try {
            final String uid = uidCompletableFuture.get(5, TimeUnit.SECONDS);
            
            if (null == uid) {
              throw new InterruptedException();
            }
            logger.debug("Connection Established for uid == {}.", uid);
          
            session.getBasicRemote().sendText("Connection established for your uid == " + uid);
          } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Rpc failure."));
          } catch (ExecutionException | TimeoutException ex) {
            logger.error(ex.getMessage(), ex);
            session.close(new CloseReason(CloseReason.CloseCodes.TRY_AGAIN_LATER, "Rpc failure."));
          } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            session.close(new CloseReason(CloseReason.CloseCodes.TRY_AGAIN_LATER, "Rpc failure."));
          }
        } else {
          session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, missingTokenHint));
        }
      } else {
        session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, missingTokenHint));
      }
    } else {
      session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, missingTokenHint));
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
    /**
     * Intentionally NOT logging the `Throwable`.
     */
  }
}