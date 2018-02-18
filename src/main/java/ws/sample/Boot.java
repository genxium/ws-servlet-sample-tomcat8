package ws.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;

public class Boot implements javax.servlet.ServletContextListener {
  
  private static final Logger logger = LoggerFactory.getLogger(Boot.class);
  private static DataSource mysqlConnDs = null;
  
  /**
   * The explicit constructor of `Boot` must be
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
  public Boot() {
  }
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    logger.info("Context has been initialized");
    
    initMySQLConnDs();
    
    // Merely testing whether `mysqlConnDs` is valid.
    /**
     * Uses the following preconfigured MySQL schema & data.
     *
     * user@shell> mysql -uroot test
     
     -------------------------------------------
     mysql> show tables;
     +----------------+
     | Tables_in_test |
     +----------------+
     | player         |
     +----------------+
     1 row in set (0.00 sec)
     
     mysql> desc player;
     +-------+--------------+------+-----+---------+----------------+
     | Field | Type         | Null | Key | Default | Extra          |
     +-------+--------------+------+-----+---------+----------------+
     | id    | int(11)      | NO   | PRI | NULL    | auto_increment |
     | name  | varchar(255) | YES  |     | NULL    |                |
     +-------+--------------+------+-----+---------+----------------+
     2 rows in set (0.04 sec)
     
     mysql> select * from player;
     +----+-------+
     | id | name  |
     +----+-------+
     |  1 | tom   |
     |  2 | cat   |
     |  3 | track |
     +----+-------+
     3 rows in set (0.01 sec)
     */
    try (final Connection mysqlConn = getMysqlConnDs().getConnection()) {
      mysqlConn.setAutoCommit(true);
      
      final String sql = "SELECT id, name FROM player";
      try (final PreparedStatement stmt = mysqlConn.prepareStatement(sql)) {
        try (final ResultSet rs = stmt.executeQuery()) {
          while (rs.next()) {
            logger.info("ID: {}, name: {}", rs.getInt("id"), rs.getString("name"));
          }
        }
        
      }
    } catch (SQLException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    logger.info("Context has been destroyed");
  }
  
  private static void initMySQLConnDs() {
    try {
      /**
       * This JNDI resource named "jdbc/mysqltester" is defined in
       * "<proj-root>/src/main/webapp/META-INF/context.xml".
       *
       * This project is deliberately NOT USING <resource-ref> element in "<proj-root>/src/main/webapp/WEB-INF/web.xml" for further mapping abstraction.
       *
       * The use of <resource-ref> is discussed in detail by JSR315 Section 14.4 item#24, and is considered unnecessary hereby.
       *
       * If resource "jdbc/mysqltester" is not properly configured, e.g. wrong connection credentials, then `mysqlConnDs` would be null.
       */
      final Context initCtx = new InitialContext();
      final Context envCtx = (Context) initCtx.lookup("java:comp/env");
      mysqlConnDs = (DataSource) envCtx.lookup("jdbc/mysqltester");
    } catch (NamingException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }
  
  public static DataSource getMysqlConnDs() {
    return mysqlConnDs;
  }
}

