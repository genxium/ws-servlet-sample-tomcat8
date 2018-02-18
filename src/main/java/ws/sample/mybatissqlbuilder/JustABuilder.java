package ws.sample.mybatissqlbuilder;

import org.apache.ibatis.jdbc.SQL;

import ws.sample.model.Player;

/**
 * Reference http://www.mybatis.org/mybatis-3/statement-builders.html.
 */
public class JustABuilder {
    public String singlePlayer() {
      return new SQL()
          .SELECT("*")
          .FROM(Player.TABLE_NAME)
          .WHERE("id = #{id}") // The param "#{id}" is acquired from `@Param("id")` of the calling "Mapper method".
          .toString();
    }
    
    public String batchPlayer() {
      return new SQL()
          .SELECT("*")
          .FROM(Player.TABLE_NAME)
          .toString();
    }
}
