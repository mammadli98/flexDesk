package flexDesk.backend.entities;

import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

@Entity
public class MultiDeskBooking {

  @Id
  @GeneratedValue
  private Long mdbId;

  private LocalDate date;
  private TimePeriod timePeriod;
  private boolean deletedFlag;

  @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.EAGER)
  private List<Booking> bookingList;

  @OneToOne(cascade = { CascadeType.REMOVE })
  private User user;

  public Long getMdbId() {
    return mdbId;
  }

  public boolean isDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(boolean deletedFalg) {
    this.deletedFlag = deletedFalg;
  }

  public List<Booking> getBookingList() {
    return bookingList;
  }

  public void setBookingList(List<Booking> bookingList) {
    this.bookingList = bookingList;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public TimePeriod getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(TimePeriod timePeriod) {
    this.timePeriod = timePeriod;
  }
}
