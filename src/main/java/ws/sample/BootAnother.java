package ws.sample;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContextEvent;

import ws.sample.model.Player;
import ws.sample.mybatismapper.JustAMapper;
import ws.sample.mybatisresulthandler.PlayerPrintingHandler;

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
      final JustAMapper justAMapper = s.getMapper(JustAMapper.class);
      
      final long targetPlayerId = 1;
      final Player selectedPlayer = justAMapper.selectPlayer(targetPlayerId);
      
      if (null != selectedPlayer) {
        logger.info("The `name` of `selectedPlayer` with `id` {} is {}.", targetPlayerId, selectedPlayer.getName());
      }
      
      final long anotherTargetPlayerId = 2;
      final Player anotherSelectedPlayer = s.selectOne("ws.sample.mybatisxmlmapper.JustAnotherMapper.selectPlayer", anotherTargetPlayerId);
      if (null != anotherSelectedPlayer) {
        logger.info("The `name` of `anotherSelectedPlayer` with `id` {} is {}.", anotherTargetPlayerId, anotherSelectedPlayer.getName());
      }
      
      final List<Player> firstTwoPlayers = justAMapper.selectPlayers(new RowBounds(0, 2));
      if (null != firstTwoPlayers) {
        logger.info("The first two players are as follows.");
        for (final Player p : firstTwoPlayers) {
          logger.info("\nid: {}\nname: {}\ndisplayName: {}\n", p.getId(), p.getName(), p.getDisplayName());
        }
      }
      
      final List<Player> lastTwoPlayers = justAMapper.selectPlayersPhysical(1, 2);
      if (null != lastTwoPlayers) {
        logger.info("The last two players are as follows.");
        for (final Player p : lastTwoPlayers) {
          logger.info("\nid: {}\nname: {}\ndisplayName: {}\n", p.getId(), p.getName(), p.getDisplayName());
        }
      }
      
      logger.info("Traversing The first two players by iterable.");
      justAMapper.iterateOverPlayers(PlayerPrintingHandler.getInstance());
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
