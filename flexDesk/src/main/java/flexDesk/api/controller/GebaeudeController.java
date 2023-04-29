package flexDesk.api.controller;

import flexDesk.api.ModelMapper;
import flexDesk.api.contract.GebaeudeDto;
import flexDesk.api.contract.interfaces.IGebaeudeController;
import flexDesk.backend.services.GebauedeService;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GebaeudeController implements IGebaeudeController {

  @Autowired
  GebauedeService gebauedeService;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public List<GebaeudeDto> gebaeude() {
    return modelMapper.gebaeudeToGebaeudeDtos(gebauedeService.gebaude());
  }

  @Override
  public GebaeudeDto gebaeudeIdToGebaeudeDto(Long gebaeudeId)
    throws GenericServiceException {
    return modelMapper.gebaeudeToGebaeudeDto(
      gebauedeService.findById(gebaeudeId)
    );
  }

  @Override
  public GebaeudeDto create(String gebaeudeName)
    throws GenericServiceException {
    return modelMapper.gebaeudeToGebaeudeDto(
      gebauedeService.create(gebaeudeName)
    );
  }
}
