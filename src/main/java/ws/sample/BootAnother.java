package ws.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

public class BootAnother implements javax.servlet.http.HttpSessionListener {
  
  private static final Logger logger = LoggerFactory.getLogger(BootAnother.class);
  
  /**
   * The explicit constructor of `BootAnother` must be
   *
   * - public, and
   * - paramless
   *
   * for being an `HttpSessionListener` registered
   * in <proj-root>/src/main/webapp/WEB-INF/web.xml.
   */
  public BootAnother() {
  
  }
  
  @Override
  public void sessionCreated(HttpSessionEvent se) {
    final HttpSession httpSession = se.getSession();
    if (null != httpSession) {
      logger.debug("HttpSession {} is created.");
    }
  }
  
  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
    final HttpSession httpSession = se.getSession();
    if (null != httpSession) {
      logger.debug("HttpSession {} is destroyed.");
    }
  }
}
