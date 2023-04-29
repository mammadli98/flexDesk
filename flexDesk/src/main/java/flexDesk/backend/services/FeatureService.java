package flexDesk.backend.services;

import flexDesk.backend.entities.Desk;
import flexDesk.backend.entities.Feature;
import flexDesk.backend.repositories.FeatureRepository;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureService {

  @Autowired
  private FeatureRepository featureRepository;

  public List<Feature> features() {
    return featureRepository.findAll();
  }

  public Feature create(String featureName) {
    Feature feature = new Feature();
    feature.setFeatureName(featureName);
    return featureRepository.save(feature);
  }

  public Feature addFeatureToDesk(Feature feature, Desk desk) {
    List<Desk> deskList = feature.getDesks();
    deskList.add(desk);
    feature.setDesks(deskList);
    return featureRepository.save(feature);
  }

  public Feature removeFeatureFromDesk(Feature feature, Desk desk) {
    List<Desk> deskList = feature.getDesks();
    deskList.remove(desk);
    feature.setDesks(deskList);
    return featureRepository.save(feature);
  }

  public Feature findById(long featureId) throws GenericServiceException {
    Optional<Feature> optionalFeature = featureRepository.findById(featureId);
    if (optionalFeature.isEmpty()) {
      throw new GenericServiceException(
        "Keine Ergebnisse für Feature mit ID: \"" + featureId + "\" gefunden"
      );
    }
    return optionalFeature.get();
  }
}
