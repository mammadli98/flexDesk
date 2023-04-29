package flexDesk.backend.services;

import flexDesk.backend.entities.Booking;
import flexDesk.backend.entities.Desk;
import flexDesk.backend.entities.User;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.repositories.BookingRepository;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

  @Autowired
  BookingRepository bookingRepository;

  public List<Booking> bookings() {
    return bookingRepository
      .findAll()
      .stream()
      .filter(booking -> booking.isDeletedFlag() == false)
      .collect(Collectors.toList());
  }

  public Booking create(
    User user,
    Desk desk,
    LocalDate bookingDate,
    TimePeriod bookingPeriod,
    boolean mdb
  ) {
    Booking booking = new Booking();
    booking.setUser(user);
    booking.setDesk(desk);
    booking.setBookingDate(bookingDate);
    booking.setBookingPeriod(bookingPeriod);
    booking.setMdb(mdb);
    booking.setDeletedFlag(false);
    return bookingRepository.save(booking);
  }

  public Booking findById(long bookingId) throws GenericServiceException {
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
    if (bookingOptional.isEmpty()) {
      throw new GenericServiceException(
        "Keine Ergebnisse für Booking mit ID: \"" + bookingId + "\" gefunden"
      );
    } else if (bookingOptional.get().isDeletedFlag()) {
      throw new GenericServiceException(
        "Booking mit ID: \"" + bookingId + "\" wurde gelöscht"
      );
    }
    return bookingOptional.get();
  }

  public Booking setDeletedFlag(Booking booking, boolean deleted) {
    booking.setDeletedFlag(deleted);
    return bookingRepository.save(booking);
  }

  public List<Booking> findByDate(LocalDate date) {
    return bookings()
      .stream()
      .filter(booking -> booking.getBookingDate().isEqual(date))
      .collect(Collectors.toList());
  }
}
