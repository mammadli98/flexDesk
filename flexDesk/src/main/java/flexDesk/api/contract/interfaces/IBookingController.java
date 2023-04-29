package flexDesk.api.contract.interfaces;

import flexDesk.api.contract.BookingDto;
import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.UserDto;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.time.LocalDate;
import java.util.List;

public interface IBookingController {
  List<BookingDto> bookings();

  BookingDto create(
    UserDto userDto,
    DeskDto deskDto,
    LocalDate bookingDate,
    TimePeriod bookingPeriod,
    boolean mdb
  )
    throws GenericServiceException;

  void delete(BookingDto bookingDto) throws GenericServiceException;
}
