package flexDesk.backend.entities;

import java.util.List;
import javax.persistence.*;

@Entity
public class Desk {

  @Id
  @GeneratedValue
  private Long deskId;

  @ManyToOne(cascade = { CascadeType.REMOVE })
  private Raum raum;

  @ManyToOne
  private Gebaeude gebaeude;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Feature> features;

  private String name;
  private float internetGeschwindigkeit; //in MBit/s
  private long popularityScore; //intialize as 0, increase by one with every booking, should be faster than counting with SQL count etc
  private boolean deletedFlag; //default = false

  public Long getDeskId() {
    return deskId;
  }

  public void setDeskId(Long deskId) {
    this.deskId = deskId;
  }

  public Raum getRaum() {
    return raum;
  }

  public void setRaum(Raum raum) {
    this.raum = raum;
  }

  public Gebaeude getGebaeude() {
    return gebaeude;
  }

  public void setGebaeude(Gebaeude gebaeude) {
    this.gebaeude = gebaeude;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public long getPopularityScore() {
    return popularityScore;
  }

  public void setPopularityScore(long popularityScore) {
    this.popularityScore = popularityScore;
  }

  public float getInternetGeschwindigkeit() {
    return internetGeschwindigkeit;
  }

  public void setInternetGeschwindigkeit(float internetGeschwindigkeit) {
    this.internetGeschwindigkeit = internetGeschwindigkeit;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }

  public boolean isDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(boolean deletedFlag) {
    this.deletedFlag = deletedFlag;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
