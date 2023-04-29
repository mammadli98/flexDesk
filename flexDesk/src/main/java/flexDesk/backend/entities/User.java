package flexDesk.backend.entities;

import flexDesk.backend.entities.derivatedAttributes.Group;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

  @Id
  @GeneratedValue
  private Long userId;

  private String userName;
  private String passwordHash;
  private Group groupMember;

  private boolean projectLead;

  private boolean deletedFlag; //default = false

  @OneToMany
  private List<Booking> bookings;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public Group getGroupMember() {
    return groupMember;
  }

  public void setGroupMember(Group groupMember) {
    this.groupMember = groupMember;
  }

  public boolean isProjectLead() {
    return projectLead;
  }

  public void setProjectLead(boolean projectLead) {
    this.projectLead = projectLead;
  }

  public boolean isDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(boolean deletedFlag) {
    this.deletedFlag = deletedFlag;
  }

  public List<Booking> getBookings() {
    return bookings;
  }

  public void setBookings(List<Booking> bookings) {
    this.bookings = bookings;
  }
}
