package ws.sample.mybatismapper;

import org.apache.ibatis.annotations.Select;

import ws.sample.model.Player;

/**
 * Reference http://www.mybatis.org/mybatis-3/getting-started.html.
 *
 * Kindly note that creating "MyBatis mapper" by "Java class" is preferred over by "XML file".
 *
 * A "MyBatis mapper" should be registered in the configs used to create the "SqlSessionFactory",
 * e.g. "<proj-root>/src/main/resources/mybatis-config.xml" in the current repository, so as to
 * become accessible from a "SqlSession" opened by that "SqlSessionFactory".
 */
public interface PlayerMapper {
  @Select("SELECT * FROM player WHERE id = #{id}")
  Player selectPlayer(long id);
}
