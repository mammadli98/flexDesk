package flexDesk.api.contract;

import flexDesk.backend.entities.Booking;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link Booking} entity
 */
public class BookingDto implements Serializable {

  private final Long bookingId;

  private boolean mdb;
  private final LocalDate bookingDate;
  private final TimePeriod bookingPeriod;
  private final UserDto user;
  private final DeskDto desk;
  private final boolean deletedFlag;

  public BookingDto(
    Long bookingId,
    boolean mdb,
    LocalDate bookingDate,
    TimePeriod bookingPeriod,
    UserDto user,
    DeskDto desk,
    boolean deletedFlag
  ) {
    this.bookingId = bookingId;
    this.mdb = mdb;
    this.bookingDate = bookingDate;
    this.bookingPeriod = bookingPeriod;
    this.user = user;
    this.desk = desk;
    this.deletedFlag = deletedFlag;
  }

  public Long getBookingId() {
    return bookingId;
  }

  public UserDto getUser() {
    return user;
  }

  public DeskDto getDesk() {
    return desk;
  }

  public boolean isDeletedFlag() {
    return deletedFlag;
  }

  public LocalDate getBookingDate() {
    return bookingDate;
  }

  public TimePeriod getBookingPeriod() {
    return bookingPeriod;
  }

  public boolean isMdb() {
    return mdb;
  }

  public void setMdb(boolean mdb) {
    this.mdb = mdb;
  }
}
