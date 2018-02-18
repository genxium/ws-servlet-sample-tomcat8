package ws.sample.mybatisresulthandler;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.sample.model.Player;

public class PlayerPrintingHandler implements ResultHandler<Player> {
  private static final Logger logger = LoggerFactory.getLogger(PlayerPrintingHandler.class);
  private static PlayerPrintingHandler instance = null;
  
  public static PlayerPrintingHandler getInstance() {
    if (null == instance) {
      instance = new PlayerPrintingHandler();
    }
    return instance;
  }
  
  @Override
  public synchronized void handleResult(ResultContext<? extends Player> resultContext) {
    if (resultContext.isStopped()) return;
  
    final Player p = resultContext.getResultObject();
    logger.info("\nid: {}\nname: {}\ndisplayName: {}\nresultCount: {}\n", p.getId(), p.getName(), p.getDisplayName(), resultContext.getResultCount());
  
    if (2 <= resultContext.getResultCount()) {
      resultContext.stop();
    }
  }
}
