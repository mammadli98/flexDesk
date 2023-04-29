package flexDesk.backend.services;

import flexDesk.backend.entities.Desk;
import flexDesk.backend.entities.Feature;
import flexDesk.backend.entities.Gebaeude;
import flexDesk.backend.entities.Raum;
import flexDesk.backend.repositories.DeskRepository;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeskService {

  @Autowired
  private DeskRepository deskRepository;

  public List<Desk> desks() {
    return deskRepository
      .findAll()
      .stream()
      .filter(desk -> desk.isDeletedFlag() == false)
      .collect(Collectors.toList());
  }

  public List<Desk> deleted_desks() {
    return deskRepository
      .findAll()
      .stream()
      .filter(desk -> desk.isDeletedFlag() == true)
      .collect(Collectors.toList());
  }

  public List<Desk> filterByGebaeude(Gebaeude gebaeude) {
    return deskRepository
      .findByGebaeude(gebaeude)
      .stream()
      .filter(desk -> !desk.isDeletedFlag())
      .collect(Collectors.toList());
  }

  public List<Desk> filterByRaum(Raum raum) {
    return deskRepository
      .findByRaum(raum)
      .stream()
      .filter(desk -> !desk.isDeletedFlag())
      .collect(Collectors.toList());
  }

  public Desk findById(Long deskId) throws GenericServiceException {
    Optional<Desk> deskOptional = deskRepository.findById(deskId);
    if (deskOptional.isEmpty()) {
      throw new GenericServiceException(
        "Keine Ergebnisse für Desk mit ID: \"" + deskId + "\" gefunden"
      );
    } else if (deskOptional.get().isDeletedFlag()) {
      throw new GenericServiceException(
        "Desk mit ID: \"" + deskId + "\" wurde gelöscht"
      );
    }
    return deskOptional.get();
  }

  public Desk create(
    Gebaeude gebaeude,
    Raum raum,
    String name,
    float internetGeschwindigkeit,
    List<Feature> features
  )
    throws GenericServiceException {
    Desk desk = new Desk();
    desk.setGebaeude(gebaeude);
    desk.setRaum(raum);
    desk.setName(name);
    desk.setInternetGeschwindigkeit(internetGeschwindigkeit);
    desk.setFeatures(features);
    desk.setPopularityScore(0);
    desk.setDeletedFlag(false);
    return deskRepository.save(desk);
  }

  public Desk edit(
    Desk desk,
    Raum raum,
    String name,
    float internetGeschwindigkeit,
    List<Feature> features
  ) {
    desk.setRaum(raum);
    desk.setName(name);
    desk.setInternetGeschwindigkeit(internetGeschwindigkeit);
    desk.setFeatures(features);
    return deskRepository.save(desk);
  }

  public Desk setDeletedFlag(Desk desk, boolean deleted) {
    desk.setDeletedFlag(deleted);
    return deskRepository.save(desk);
  }

  public Desk setBookingFlag(Desk desk, boolean booked) {
    return deskRepository.save(desk);
  }

  public Desk incrementPopularity(Desk desk) throws GenericServiceException {
    desk.setPopularityScore(desk.getPopularityScore() + 1);
    return deskRepository.save(desk);
  }

  public Desk setPopularity(Desk desk, Long popularity)
    throws GenericServiceException {
    desk.setPopularityScore(popularity);
    return deskRepository.save(desk);
  }
}
