package flexDesk.api.controller;

import flexDesk.api.ModelMapper;
import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.FeatureDto;
import flexDesk.api.contract.interfaces.IFeatureController;
import flexDesk.backend.services.DeskService;
import flexDesk.backend.services.FeatureService;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeatureController implements IFeatureController {

  @Autowired
  FeatureService featureService;

  @Autowired
  DeskService deskService;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public List<FeatureDto> features() {
    return modelMapper.featuresToFeatureDtos(featureService.features());
  }

  @Override
  public FeatureDto featureIdToFeatureDto(long featureId)
    throws GenericServiceException {
    return modelMapper.featureToFeatureDto(featureService.findById(featureId));
  }

  @Override
  public FeatureDto create(String featureName) {
    return modelMapper.featureToFeatureDto(featureService.create(featureName));
  }

  @Override
  public void addFeatureToDesk(FeatureDto featureDto, DeskDto deskDto)
    throws GenericServiceException {
    featureService.addFeatureToDesk(
      featureService.findById(featureDto.getFeatureId()),
      deskService.findById(deskDto.getDeskId())
    );
  }

  @Override
  public void removeFeatureFromDesk(FeatureDto featureDto, DeskDto deskDto)
    throws GenericServiceException {
    featureService.removeFeatureFromDesk(
      featureService.findById(featureDto.getFeatureId()),
      deskService.findById(deskDto.getDeskId())
    );
  }
}
