package flexDesk.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Raum {

  @Id
  @GeneratedValue
  private Long raumId;

  @ManyToOne
  private Gebaeude gebaeude;

  private String raumName;

  private boolean deletedFlag; //default = false

  public Long getRaumId() {
    return raumId;
  }

  public void setRaumId(Long raumId) {
    this.raumId = raumId;
  }

  public Gebaeude getGebaeude() {
    return gebaeude;
  }

  public void setGebaeude(Gebaeude gebaeude) {
    this.gebaeude = gebaeude;
  }

  public String getRaumName() {
    return raumName;
  }

  public void setRaumName(String raumName) {
    this.raumName = raumName;
  }

  public boolean isDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(boolean deletedFlag) {
    this.deletedFlag = deletedFlag;
  }
}
