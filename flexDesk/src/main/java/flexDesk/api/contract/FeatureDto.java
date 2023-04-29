package flexDesk.api.contract;

import flexDesk.api.contract.interfaces.Selectable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link flexdesk.backend.entities.Feature} entity
 */
public class FeatureDto implements Serializable, Selectable {

  private final Long featureId;
  private final String featureName;
  private final List<DeskDto> desks;

  public FeatureDto(Long featureId, String featureName) {
    this.featureId = featureId;
    this.featureName = featureName;
    this.desks = new ArrayList<DeskDto>();
  }

  public Long getFeatureId() {
    return featureId;
  }

  public String getFeatureName() {
    return featureName;
  }

  public List<DeskDto> getDesks() {
    return desks;
  }

  @Override
  public Long getSelectableId() {
    return getFeatureId();
  }

  @Override
  public String getSelectableName() {
    return getFeatureName();
  }
}
