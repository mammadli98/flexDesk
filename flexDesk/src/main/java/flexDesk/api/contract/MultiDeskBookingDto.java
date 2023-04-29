package flexDesk.api.contract;

import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import java.time.LocalDate;
import java.util.List;

public class MultiDeskBookingDto {

  private final Long mdbId;
  private final LocalDate date;
  private final TimePeriod timePeriod;
  private final boolean deletedFlag;
  private final List<BookingDto> bookingList;
  private final UserDto user;

  public MultiDeskBookingDto(
    Long mdbId,
    LocalDate date,
    TimePeriod timePeriod,
    UserDto user,
    List<BookingDto> bookingList,
    boolean deletedFlag
  ) {
    this.mdbId = mdbId;
    this.date = date;
    this.timePeriod = timePeriod;
    this.user = user;
    this.bookingList = bookingList;
    this.deletedFlag = deletedFlag;
  }

  public Long getMdbId() {
    return mdbId;
  }

  public boolean isDeletedFlag() {
    return deletedFlag;
  }

  public List<BookingDto> getBookingList() {
    return bookingList;
  }

  public UserDto getUser() {
    return user;
  }

  public LocalDate getDate() {
    return date;
  }

  public TimePeriod getTimePeriod() {
    return timePeriod;
  }
}
