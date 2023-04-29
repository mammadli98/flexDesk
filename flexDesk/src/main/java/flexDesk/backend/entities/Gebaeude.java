package flexDesk.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Gebaeude {

  @Id
  @GeneratedValue
  private Long gebaeudeId;

  private String gebaeudeName;

  public Long getGebaeudeId() {
    return gebaeudeId;
  }

  public void setGebaeudeId(Long gebaeudeId) {
    this.gebaeudeId = gebaeudeId;
  }

  public String getGebaeudeName() {
    return gebaeudeName;
  }

  public void setGebaeudeName(String gebaeudeName) {
    this.gebaeudeName = gebaeudeName;
  }
}
