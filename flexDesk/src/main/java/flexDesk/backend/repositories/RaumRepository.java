package flexDesk.backend.repositories;

import flexDesk.backend.entities.Gebaeude;
import flexDesk.backend.entities.Raum;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaumRepository extends CrudRepository<Raum, Long> {
  List<Raum> findAll();

  List<Raum> findByGebaeude(Gebaeude gebaeude);
}
