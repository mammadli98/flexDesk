package flexDesk.api.contract.interfaces;

import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.FeatureDto;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;

public interface IFeatureController {
  List<FeatureDto> features();

  FeatureDto featureIdToFeatureDto(long featureId)
    throws GenericServiceException;

  FeatureDto create(String featureName);

  void addFeatureToDesk(FeatureDto featureDto, DeskDto deskDto)
    throws GenericServiceException;

  void removeFeatureFromDesk(FeatureDto featureDto, DeskDto deskDto)
    throws GenericServiceException;
}
