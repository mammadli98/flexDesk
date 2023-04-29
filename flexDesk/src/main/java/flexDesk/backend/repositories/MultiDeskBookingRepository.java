package flexDesk.backend.repositories;

import flexDesk.backend.entities.MultiDeskBooking;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiDeskBookingRepository
  extends CrudRepository<MultiDeskBooking, Long> {
  List<MultiDeskBooking> findAll();
}
