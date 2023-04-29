package flexDesk.backend.repositories;

import flexDesk.backend.entities.Desk;
import flexDesk.backend.entities.Gebaeude;
import flexDesk.backend.entities.Raum;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeskRepository extends CrudRepository<Desk, Long> {
  List<Desk> findAll();

  List<Desk> findByGebaeude(Gebaeude gebaeude);

  List<Desk> findByRaum(Raum raum);
}
