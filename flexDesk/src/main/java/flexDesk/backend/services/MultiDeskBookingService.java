package flexDesk.backend.services;

import flexDesk.backend.entities.Booking;
import flexDesk.backend.entities.MultiDeskBooking;
import flexDesk.backend.entities.User;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.repositories.MultiDeskBookingRepository;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MultiDeskBookingService {

  @Autowired
  MultiDeskBookingRepository multiDeskBookingRepository;

  public List<MultiDeskBooking> multiDeskBookings() {
    return multiDeskBookingRepository
      .findAll()
      .stream()
      .filter(mdb -> mdb.isDeletedFlag() == false)
      .collect(Collectors.toList());
  }

  public MultiDeskBooking create(
    User user,
    LocalDate date,
    TimePeriod timePeriod,
    List<Booking> bookingList
  ) {
    MultiDeskBooking mdb = new MultiDeskBooking();
    mdb.setUser(user);
    mdb.setDate(date);
    mdb.setTimePeriod(timePeriod);
    mdb.setBookingList(bookingList);
    mdb.setDeletedFlag(false);
    return multiDeskBookingRepository.save(mdb);
  }

  public MultiDeskBooking findById(long bookingId)
    throws GenericServiceException {
    Optional<MultiDeskBooking> mdb = multiDeskBookingRepository.findById(
      bookingId
    );
    if (mdb.isEmpty()) {
      throw new GenericServiceException(
        "Keine Ergebnisse für Booking mit ID: \"" + bookingId + "\" gefunden"
      );
    } else if (mdb.get().isDeletedFlag()) {
      throw new GenericServiceException(
        "Booking mit ID: \"" + bookingId + "\" wurde gelöscht"
      );
    }
    return mdb.get();
  }

  public MultiDeskBooking setDeletedFlag(
    MultiDeskBooking mdb,
    boolean deleted
  ) {
    mdb.setDeletedFlag(deleted);
    return multiDeskBookingRepository.save(mdb);
  }
}
