package flexDesk.api.contract.interfaces;

import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.FeatureDto;
import flexDesk.api.contract.GebaeudeDto;
import flexDesk.api.contract.RaumDto;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;

public interface IDeskController {
  List<DeskDto> desks() throws GenericServiceException;

  List<DeskDto> deleted_desks();

  DeskDto deskIdToDeskDto(long deskId) throws GenericServiceException;

  List<DeskDto> filterByGebaeude(GebaeudeDto gebaeudeDto)
    throws GenericServiceException;

  List<DeskDto> filterByRaum(RaumDto raumDto) throws GenericServiceException;

  DeskDto create(
    GebaeudeDto gebaeudeDto,
    RaumDto raumDto,
    String name,
    float internetGeschwindigkeit,
    List<FeatureDto> featureDtos
  )
    throws GenericServiceException;

  void edit(
    DeskDto deskDto,
    RaumDto raumDto,
    String name,
    float internetGeschwindigkeit,
    List<FeatureDto> featureDtos
  )
    throws GenericServiceException;

  void delete(DeskDto deskDto) throws GenericServiceException;

  void restore(DeskDto deskDto) throws GenericServiceException;
}
