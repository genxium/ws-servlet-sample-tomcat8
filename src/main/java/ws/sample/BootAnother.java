package ws.sample;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContextEvent;

import ws.sample.model.Player;
import ws.sample.mybatismapper.PlayerMapper;

public class BootAnother implements javax.servlet.ServletContextListener {
  
  private static final Logger logger = LoggerFactory.getLogger(BootAnother.class);
  private static SqlSessionFactory sqlSessionFactory = null;
  
  /**
   * The explicit constructor of `BootAnother` must be
   *
   * - public, and
   * - paramless
   *
   * for being a `ServletContextListener` registered
   * in <proj-root>/src/main/webapp/WEB-INF/web.xml.
   *
   * Moreover, being a subclass of `ServletContextListener`, this class SHOULDN'T follow
   * the "singleton pattern" or be instantiated explicitly by any other means,
   * because it's ONLY supposed to be instantiated implicitly by the "apache-tomcat-* container".
   */
  public BootAnother() {
  
  }
  
  
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    logger.info("Context has been initialized");
  
    initSqlSessionFactory();
  
    // Merely testing whether `sqlSessionFactory` is valid.
    try (final SqlSession s = sqlSessionFactory.openSession(true)) {
      final PlayerMapper playerMapper = s.getMapper(ws.sample.mybatismapper.PlayerMapper.class);
      
      final long targetPlayerId = 1;
      final Player selectedPlayer = playerMapper.selectPlayer(targetPlayerId);
      
      if (null != selectedPlayer) {
        logger.info("The `name` of `selectedPlayer` with `id` {} is {}.", targetPlayerId, selectedPlayer.getName());
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    logger.info("Context has been destroyed");
  }
  
  private static void initSqlSessionFactory() {
    try (final InputStream in = Resources.getResourceAsStream("mybatis-config.xml")) {
      /**
       * Will be using the "default" of "environments", i.e. "development" in this case.
       * See source of "org.apache.ibatis.builder.xml.XMLConfigBuilder" for details.
       */
    
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }
    
  public static SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }
}
