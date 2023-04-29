package flexDesk.backend.services;

import flexDesk.backend.entities.Gebaeude;
import flexDesk.backend.entities.Raum;
import flexDesk.backend.repositories.RaumRepository;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RaumService {

  @Autowired
  private RaumRepository raumRepository;

  public List<Raum> rooms() {
    return raumRepository
      .findAll()
      .stream()
      .filter(raum -> raum.isDeletedFlag() == false)
      .collect(Collectors.toList());
  }

  public List<Raum> filterByGebaude(Gebaeude gebaeude) {
    return raumRepository.findByGebaeude(gebaeude);
  }

  public List<Raum> filterId(int filterId) {
    return raumRepository
      .findAll()
      .stream()
      .filter(raum -> raum.getRaumId() == filterId)
      .collect(Collectors.toList());
  }

  public List<Raum> searchedRooms(String searchString) {
    return this.rooms()
      .stream()
      .filter(raum -> raum.getRaumName().contains(searchString))
      .collect(Collectors.toList());
  }

  public Raum create(Gebaeude gebaeude, String raumName) {
    Raum raum = new Raum();
    raum.setGebaeude(gebaeude);
    raum.setRaumName(raumName);
    raum.setDeletedFlag(false);
    return raumRepository.save(raum);
  }

  public Raum setDeletedFlag(Raum raum, boolean deleted) {
    raum.setDeletedFlag(deleted);
    return raumRepository.save(raum);
  }

  public Raum findById(Long raumId) throws GenericServiceException {
    Optional<Raum> optionalRaum = raumRepository.findById(raumId);
    if (optionalRaum.isEmpty()) {
      throw new GenericServiceException(
        "Keine Ergebnisse für Raum mit ID: \"" + raumId + "\" gefunden"
      );
    } else if (optionalRaum.get().isDeletedFlag()) {
      throw new GenericServiceException(
        "Raum mit ID: \"" + raumId + "\" wurde gelöscht"
      );
    }

    return optionalRaum.get();
  }
}
