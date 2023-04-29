package flexDesk.backend.services;

import flexDesk.backend.entities.Gebaeude;
import flexDesk.backend.repositories.GebaeudeRepository;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GebauedeService {

  @Autowired
  private GebaeudeRepository gebaeudeRepository;

  public List<Gebaeude> gebaude() {
    return gebaeudeRepository.findAll();
  }

  public Gebaeude create(String gebaudeName) {
    Gebaeude gebaeude = new Gebaeude();
    gebaeude.setGebaeudeName(gebaudeName);
    return gebaeudeRepository.save(gebaeude);
  }

  public Gebaeude findById(Long gebaeudeId) throws GenericServiceException {
    Optional<Gebaeude> optionalGebaeude = gebaeudeRepository.findById(
      gebaeudeId
    );
    if (optionalGebaeude.isEmpty()) {
      throw new GenericServiceException(
        "Keine Ergebnisse für Gebäude mit ID: \"" + gebaeudeId + "\" gefunden"
      );
    }
    return optionalGebaeude.get();
  }
}
