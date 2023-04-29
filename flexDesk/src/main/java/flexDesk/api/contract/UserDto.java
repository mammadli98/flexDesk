package flexDesk.api.contract;

import flexDesk.backend.entities.derivatedAttributes.Group;
import java.io.Serializable;

/**
 * A DTO for the {@link flexdesk.backend.entities.User} entity
 */
public class UserDto implements Serializable {

  private final Long userId;
  private final String userName;
  private final String passwordHash;
  private final Group groupMember;

  private final boolean projectLead;
  private final boolean deletedFlag;

  public UserDto(
    Long userId,
    String userName,
    String passwordHash,
    Group groupMember,
    boolean projectLead,
    boolean deletedFlag
  ) {
    this.userId = userId;
    this.userName = userName;
    this.passwordHash = passwordHash;
    this.groupMember = groupMember;
    this.projectLead = projectLead;
    this.deletedFlag = deletedFlag;
  }

  public Long getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public Group getGroupMember() {
    return groupMember;
  }

  public boolean getDeletedFlag() {
    return deletedFlag;
  }

  public boolean isProjectLead() {
    return projectLead;
  }
}
