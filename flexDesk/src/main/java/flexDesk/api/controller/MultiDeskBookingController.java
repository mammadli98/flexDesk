package flexDesk.api.controller;

import flexDesk.api.ModelMapper;
import flexDesk.api.contract.BookingDto;
import flexDesk.api.contract.MultiDeskBookingDto;
import flexDesk.api.contract.UserDto;
import flexDesk.api.contract.interfaces.IMultiDeskBookingController;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.services.BookingService;
import flexDesk.backend.services.MultiDeskBookingService;
import flexDesk.backend.services.UserService;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultiDeskBookingController implements IMultiDeskBookingController {

  @Autowired
  MultiDeskBookingService multiDeskBookingService;

  @Autowired
  UserService userService;

  @Autowired
  BookingService bookingService;

  @Autowired
  ModelMapper modelMapper;

  public List<MultiDeskBookingDto> multiDeskBookings() {
    return modelMapper.mdbsToMdbDtos(
      multiDeskBookingService.multiDeskBookings()
    );
  }

  public MultiDeskBookingDto create(
    UserDto userDto,
    LocalDate date,
    TimePeriod timePeriod,
    List<BookingDto> bookingDtoList
  )
    throws GenericServiceException {
    return modelMapper.mdbToMdbDto(
      multiDeskBookingService.create(
        userService.findById(userDto.getUserId()),
        date,
        timePeriod,
        bookingDtoList
          .stream()
          .map(BookingDto::getBookingId)
          .map(
            id -> {
              try {
                return bookingService.findById(id);
              } catch (GenericServiceException e) {
                throw new RuntimeException(e);
              }
            }
          )
          .collect(Collectors.toList())
      )
    );
  }

  public void delete(MultiDeskBookingDto multiDeskBookingDto) {
    multiDeskBookingDto
      .getBookingList()
      .stream()
      .forEach(
        bookingDto -> {
          try {
            bookingService.setDeletedFlag(
              bookingService.findById(bookingDto.getBookingId()),
              true
            );
          } catch (GenericServiceException e) {
            System.err.println(e.getCause() + e.getMessage());
          }
        }
      );
    try {
      multiDeskBookingService.setDeletedFlag(
        multiDeskBookingService.findById(multiDeskBookingDto.getMdbId()),
        true
      );
    } catch (GenericServiceException e) {
      System.err.println(e.getCause() + e.getMessage());
    }
  }
}
