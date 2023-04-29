package flexDesk.backend.repositories;

import flexDesk.backend.entities.Booking;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
  List<Booking> findAll();
}
