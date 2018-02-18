package ws.sample.model;

public class Player {
  public static final String TABLE_NAME = "player";
  
  private long id;
  private String name;
  private String displayName; // A redundant field for demostrating how "MyBatis Mapper Annotation @Result" works.
  
  public long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public String getDisplayName() {
    return displayName;
  }
}
