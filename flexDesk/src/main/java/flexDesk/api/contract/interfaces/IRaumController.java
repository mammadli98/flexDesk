package flexDesk.api.contract.interfaces;

import flexDesk.api.contract.GebaeudeDto;
import flexDesk.api.contract.RaumDto;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;

public interface IRaumController {
  List<RaumDto> rooms() throws GenericServiceException;

  RaumDto raumIdToRaumDto(long raumId) throws GenericServiceException;

  List<RaumDto> filterByGebaude(GebaeudeDto gebaeudeDto)
    throws GenericServiceException;

  RaumDto create(GebaeudeDto gebaeudeDto, String raumName)
    throws GenericServiceException;
}
