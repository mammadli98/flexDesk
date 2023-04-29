package flexDesk.api.controller;

import flexDesk.api.ModelMapper;
import flexDesk.api.contract.BookingDto;
import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.UserDto;
import flexDesk.api.contract.interfaces.IBookingController;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.services.BookingService;
import flexDesk.backend.services.DeskService;
import flexDesk.backend.services.UserService;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingController implements IBookingController {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private DeskService deskService;

  @Autowired
  private UserService userService;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public List<BookingDto> bookings() {
    return modelMapper.bookingsToBookingDtos(bookingService.bookings());
  }

  @Override
  public BookingDto create(
    UserDto userDto,
    DeskDto deskDto,
    LocalDate bookingDate,
    TimePeriod bookingPeriod,
    boolean mdb
  )
    throws GenericServiceException {
    BookingDto bookingDto = modelMapper.bookingToBookingDto(
      bookingService.create(
        userService.findById(userDto.getUserId()),
        deskService.findById(deskDto.getDeskId()),
        bookingDate,
        bookingPeriod,
        mdb
      )
    );
    deskService.setBookingFlag(deskService.findById(deskDto.getDeskId()), true);
    deskService.incrementPopularity(deskService.findById(deskDto.getDeskId()));
    return bookingDto;
  }

  @Override
  public void delete(BookingDto bookingDto) throws GenericServiceException {
    deskService.setBookingFlag(
      deskService.findById(bookingDto.getDesk().getDeskId()),
      false
    );
    bookingService.setDeletedFlag(
      bookingService.findById(bookingDto.getBookingId()),
      true
    );
  }

  public List<BookingDto> findByDate(LocalDate date) {
    return modelMapper.bookingsToBookingDtos(bookingService.findByDate(date));
  }
}
