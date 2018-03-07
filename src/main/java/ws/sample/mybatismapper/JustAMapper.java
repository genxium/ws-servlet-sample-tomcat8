package ws.sample.mybatismapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

import ws.sample.model.Player;
import ws.sample.mybatissqlbuilder.JustABuilder;

/**
 * Reference http://www.mybatis.org/mybatis-3/getting-started.html.
 *
 * Kindly note that creating "MyBatis mapper" by "Java class" is preferred over by "XML file".
 *
 * However due to the introduction of "Mapper Annotations" in
 * http://www.mybatis.org/mybatis-3/java-api.html, there're some syntax limitations from
 * "Java Annotation" which makes "MyBatis Mapper Annotations" not as powerful as the traditional
 * XML alternative http://www.mybatis.org/mybatis-3/sqlmap-xml.html.
 *
 * A "MyBatis mapper" should be registered in the configs used to create the "SqlSessionFactory",
 * e.g. "<proj-root>/src/main/resources/mybatis-config.xml" in the current repository, so as to
 * become accessible from a "SqlSession" opened by that "SqlSessionFactory".
 */
public interface JustAMapper {
  /*
  @Select("SELECT * FROM player WHERE id = #{id}")
  Player selectPlayer(long id);
  */
  
  @Results(id = "awkwardNamedResultsForPlayerClass", value = {
      @Result(property = "id", column = "id", id = true),
      @Result(property = "name", column = "name"),
      @Result(property = "displayName", column = "name")
  })
  @SelectProvider(type = JustABuilder.class, method = "singlePlayer")
  Player selectPlayer(@Param("id") final long id);
  
  /**
   * A `@Results(id="foo", value = ...)` annotation can be reused later by
   * `@ResultMap("foo")`.
   */
  /**
   * Making use of the "physical pagination" provided by JDBC.
   */
  @ResultMap("awkwardNamedResultsForPlayerClass")
  @SelectProvider(type = JustABuilder.class, method = "batchPlayerPhysical")
  List<Player> selectPlayersPhysical(@Param("offset") final int offset, @Param("count") final int count);
  
  /**
   * Making use of the "logical pagination" provided by MyBatis3.
   */
  @ResultMap("awkwardNamedResultsForPlayerClass")
  @SelectProvider(type = JustABuilder.class, method = "batchPlayer")
  List<Player> selectPlayers(final RowBounds rowBounds);
  
  /**
   * Use a "forward-only iterable of query results" For handling predictable large amount of results, e.g. when traversing the whole table during a cronjob.
   */
  @ResultMap("awkwardNamedResultsForPlayerClass")
  @SelectProvider(type = JustABuilder.class, method = "batchPlayer")
  void iterateOverPlayers(final ResultHandler rh);
}
