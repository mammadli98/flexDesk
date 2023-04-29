package flexDesk.api.controller;

import flexDesk.api.ModelMapper;
import flexDesk.api.contract.GebaeudeDto;
import flexDesk.api.contract.RaumDto;
import flexDesk.api.contract.interfaces.IRaumController;
import flexDesk.backend.services.GebauedeService;
import flexDesk.backend.services.RaumService;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RaumController implements IRaumController {

  @Autowired
  private RaumService raumService;

  @Autowired
  private GebauedeService gebauedeService;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public List<RaumDto> rooms() {
    return modelMapper.roomsToRoomDtos(raumService.rooms());
  }

  @Override
  public RaumDto raumIdToRaumDto(long raumId) throws GenericServiceException {
    return modelMapper.raumToRaumDto(raumService.findById(raumId));
  }

  @Override
  public List<RaumDto> filterByGebaude(GebaeudeDto gebaeudeDto)
    throws GenericServiceException {
    return modelMapper.roomsToRoomDtos(
      raumService.filterByGebaude(
        gebauedeService.findById(gebaeudeDto.getGebaeudeId())
      )
    );
  }

  @Override
  public RaumDto create(GebaeudeDto gebaeudeDto, String raumName)
    throws GenericServiceException {
    return modelMapper.raumToRaumDto(
      raumService.create(
        gebauedeService.findById(gebaeudeDto.getGebaeudeId()),
        raumName
      )
    );
  }

  public void delete(RaumDto raumDto) throws GenericServiceException {
    raumService.setDeletedFlag(raumService.findById(raumDto.getRaumId()), true);
  }

  public void restore(RaumDto raumDto) throws GenericServiceException {
    raumService.setDeletedFlag(
      raumService.findById(raumDto.getRaumId()),
      false
    );
  }
}
