package flexDesk.backend.repositories;

import flexDesk.backend.entities.Gebaeude;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GebaeudeRepository extends CrudRepository<Gebaeude, Long> {
  List<Gebaeude> findAll();
}
