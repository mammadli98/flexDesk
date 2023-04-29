package flexDesk.backend.entities;

import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import java.time.LocalDate;
import javax.persistence.*;

@Entity
public class Booking {

  @Id
  @GeneratedValue
  private Long bookingId;

  @ManyToOne(cascade = { CascadeType.REMOVE })
  private User user;

  @OneToOne(cascade = { CascadeType.REMOVE })
  private Desk desk;

  private boolean mdb;
  private LocalDate bookingDate;
  private TimePeriod bookingPeriod;
  private boolean deletedFlag; //default = false

  public boolean isMdb() {
    return mdb;
  }

  public void setMdb(boolean mdb) {
    this.mdb = mdb;
  }

  public Long getBookingId() {
    return bookingId;
  }

  public void setBookingId(Long bookingId) {
    this.bookingId = bookingId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Desk getDesk() {
    return desk;
  }

  public void setDesk(Desk desk) {
    this.desk = desk;
  }

  public LocalDate getBookingDate() {
    return bookingDate;
  }

  public void setBookingDate(LocalDate bookingDate) {
    this.bookingDate = bookingDate;
  }

  public TimePeriod getBookingPeriod() {
    return bookingPeriod;
  }

  public void setBookingPeriod(TimePeriod bookingPeriod) {
    this.bookingPeriod = bookingPeriod;
  }

  public boolean isDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(boolean deletedFlag) {
    this.deletedFlag = deletedFlag;
  }
}
