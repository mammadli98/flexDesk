package flexDesk.backend.entities;

import java.util.List;
import javax.persistence.*;

@Entity
public class Feature {

  @Id
  @GeneratedValue
  private Long featureId;

  private String featureName;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Desk> desks;

  public Long getFeatureId() {
    return featureId;
  }

  public void setFeatureId(Long featureId) {
    this.featureId = featureId;
  }

  public String getFeatureName() {
    return featureName;
  }

  public void setFeatureName(String featureName) {
    this.featureName = featureName;
  }

  public List<Desk> getDesks() {
    return desks;
  }

  public void setDesks(List<Desk> desks) {
    this.desks = desks;
  }
}
