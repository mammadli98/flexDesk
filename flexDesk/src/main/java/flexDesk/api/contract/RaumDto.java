package flexDesk.api.contract;

import flexDesk.api.contract.interfaces.Selectable;
import java.io.Serializable;

/**
 * A DTO for the {@link flexdesk.backend.entities.Raum} entity
 */
public class RaumDto implements Serializable, Selectable {

  private final Long raumId;
  private final GebaeudeDto gebaeude;
  private final String raumName;
  private final boolean deletedFlag;

  public RaumDto(
    Long raumId,
    GebaeudeDto gebaeude,
    String raumName,
    boolean deletedFlag
  ) {
    this.raumId = raumId;
    this.gebaeude = gebaeude;
    this.raumName = raumName;
    this.deletedFlag = deletedFlag;
  }

  public Long getRaumId() {
    return raumId;
  }

  public GebaeudeDto getGebaeude() {
    return gebaeude;
  }

  public String getRaumName() {
    return raumName;
  }

  public boolean getDeletedFlag() {
    return deletedFlag;
  }

  @Override
  public Long getSelectableId() {
    return getRaumId();
  }

  @Override
  public String getSelectableName() {
    return getRaumName();
  }
}
