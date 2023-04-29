package flexDesk.backend.repositories;

import flexDesk.backend.entities.Feature;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends CrudRepository<Feature, Long> {
  List<Feature> findAll();
}
