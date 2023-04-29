package flexDesk.api.contract.interfaces;

import flexDesk.api.contract.GebaeudeDto;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;

public interface IGebaeudeController {
  List<GebaeudeDto> gebaeude();

  GebaeudeDto gebaeudeIdToGebaeudeDto(Long gebaeudeId)
    throws GenericServiceException;

  GebaeudeDto create(String gebaeudeName) throws GenericServiceException;
}
