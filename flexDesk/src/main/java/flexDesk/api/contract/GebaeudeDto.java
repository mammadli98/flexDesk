package flexDesk.api.contract;

import flexDesk.api.contract.interfaces.Selectable;
import java.io.Serializable;

/**
 * A DTO for the {@link flexdesk.backend.entities.Gebaeude} entity
 */
public class GebaeudeDto implements Serializable, Selectable {

  private final Long gebaeudeId;
  private final String gebaeudeName;

  public GebaeudeDto(Long gebaeudeId, String gebaeudeName) {
    this.gebaeudeId = gebaeudeId;
    this.gebaeudeName = gebaeudeName;
  }

  public Long getGebaeudeId() {
    return gebaeudeId;
  }

  public String getGebaeudeName() {
    return gebaeudeName;
  }

  @Override
  public Long getSelectableId() {
    return getGebaeudeId();
  }

  @Override
  public String getSelectableName() {
    return gebaeudeName;
  }
}
