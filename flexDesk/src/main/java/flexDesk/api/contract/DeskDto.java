package flexDesk.api.contract;

import flexDesk.backend.entities.*;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link Desk} entity
 */
public class DeskDto implements Serializable {

  private final Long deskId;
  private final RaumDto raum;
  private final GebaeudeDto gebaeude;

  private List<FeatureDto> features;

  private final String name;
  private final float internetGeschwindigkeit;
  private final long popularityScore;
  private final boolean deletedFlag;

  public DeskDto(
    Long deskId,
    RaumDto raum,
    GebaeudeDto gebaeude,
    String name,
    float internetGeschwindigkeit,
    List<FeatureDto> features,
    long popularityScore,
    boolean deletedFlag
  ) {
    this.deskId = deskId;
    this.raum = raum;
    this.gebaeude = gebaeude;
    this.name = name;
    this.internetGeschwindigkeit = internetGeschwindigkeit;
    this.features = features;
    this.popularityScore = popularityScore;
    this.deletedFlag = deletedFlag;
  }

  public Long getDeskId() {
    return deskId;
  }

  public RaumDto getRaum() {
    return raum;
  }

  public GebaeudeDto getGebaeude() {
    return gebaeude;
  }

  public float getInternetGeschwindigkeit() {
    return internetGeschwindigkeit;
  }

  public String getName() {
    return name;
  }

  public long getPopularityScore() {
    return popularityScore;
  }

  public List<FeatureDto> getFeatures() {
    return features;
  }

  public void setFeatures(List<FeatureDto> features) {
    this.features = features;
  }

  public boolean isDeletedFlag() {
    return deletedFlag;
  }
}
