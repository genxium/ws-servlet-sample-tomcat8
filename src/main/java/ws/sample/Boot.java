package ws.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

public class Boot implements javax.servlet.ServletContextListener {
  
  private static final Logger logger = LoggerFactory.getLogger(Boot.class);
  
  /**
   * The explicit constructor of `Boot` must be
   *
   * - public, and
   * - paramless
   *
   * for being a `ServletContextListener` registered
   * in <proj-root>/src/main/webapp/WEB-INF/web.xml.
   */
  public Boot() {
  }
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    logger.info("Context has been initialized");
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    logger.info("Context has been destroyed");
  }
}

