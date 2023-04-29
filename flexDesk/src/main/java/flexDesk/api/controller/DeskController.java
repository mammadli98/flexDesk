package flexDesk.api.controller;

import flexDesk.api.ModelMapper;
import flexDesk.api.contract.*;
import flexDesk.api.contract.interfaces.IDeskController;
import flexDesk.backend.services.DeskService;
import flexDesk.backend.services.FeatureService;
import flexDesk.backend.services.GebauedeService;
import flexDesk.backend.services.RaumService;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeskController implements IDeskController {

  @Autowired
  private DeskService deskService;

  @Autowired
  private GebauedeService gebauedeService;

  @Autowired
  private RaumService raumService;

  @Autowired
  private FeatureService featureService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  BookingController bookingController;

  @Override
  public List<DeskDto> desks() {
    return modelMapper.desksToDeskDtos(deskService.desks());
  }

  @Override
  public List<DeskDto> deleted_desks() {
    return modelMapper.desksToDeskDtos(deskService.deleted_desks());
  }

  @Override
  public DeskDto deskIdToDeskDto(long deskId) throws GenericServiceException {
    return modelMapper.deskToDeskDto(deskService.findById(deskId));
  }

  @Override
  public List<DeskDto> filterByGebaeude(GebaeudeDto gebaeudeDto)
    throws GenericServiceException {
    return modelMapper.desksToDeskDtos(
      deskService.filterByGebaeude(
        gebauedeService.findById(gebaeudeDto.getGebaeudeId())
      )
    );
  }

  @Override
  public List<DeskDto> filterByRaum(RaumDto raumDto)
    throws GenericServiceException {
    return modelMapper.desksToDeskDtos(
      deskService.filterByRaum(raumService.findById(raumDto.getRaumId()))
    );
  }

  @Override
  public DeskDto create(
    GebaeudeDto gebaeudeDto,
    RaumDto raumDto,
    String name,
    float internetGeschwindigkeit,
    List<FeatureDto> featureDtos
  )
    throws GenericServiceException {
    DeskDto deskDto = modelMapper.deskToDeskDto(
      deskService.create(
        gebauedeService.findById(gebaeudeDto.getGebaeudeId()),
        raumService.findById(raumDto.getRaumId()),
        name,
        internetGeschwindigkeit,
        featureDtos
          .stream()
          .map(
            featureDto -> {
              try {
                return featureService.findById(featureDto.getFeatureId());
              } catch (GenericServiceException e) {
                throw new RuntimeException(e);
              }
            }
          )
          .collect(Collectors.toList())
      )
    );
    for (FeatureDto featureDto : featureDtos) {
      featureService.addFeatureToDesk(
        featureService.findById(featureDto.getFeatureId()),
        deskService.findById(deskDto.getDeskId())
      );
    }
    return deskDto;
  }

  @Override
  public void edit(
    DeskDto deskDto,
    RaumDto raumDto,
    String name,
    float internetGeschwindigkeit,
    List<FeatureDto> featureDtos
  )
    throws GenericServiceException {
    List<FeatureDto> selectedFeatures = deskDto.getFeatures();

    List<FeatureDto> featuresToRemove = selectedFeatures
      .stream()
      .filter(featureDto -> !featureDtos.contains(featureDto))
      .collect(Collectors.toList());

    List<FeatureDto> featuresToAdd = featureDtos
      .stream()
      .filter(featureDto -> !selectedFeatures.contains(featureDto))
      .collect(Collectors.toList());

    for (FeatureDto featureDto : featuresToRemove) {
      featureService.removeFeatureFromDesk(
        featureService.findById(featureDto.getFeatureId()),
        deskService.findById(deskDto.getDeskId())
      );
    }

    for (FeatureDto featureDto : featuresToAdd) {
      featureService.addFeatureToDesk(
        featureService.findById(featureDto.getFeatureId()),
        deskService.findById(deskDto.getDeskId())
      );
    }

    deskService.edit(
      deskService.findById(deskDto.getDeskId()),
      raumService.findById(raumDto.getRaumId()),
      name,
      internetGeschwindigkeit,
      featureDtos
        .stream()
        .map(
          featureDto -> {
            try {
              return featureService.findById(featureDto.getFeatureId());
            } catch (GenericServiceException e) {
              throw new RuntimeException(e);
            }
          }
        )
        .collect(Collectors.toList())
    );
  }

  @Override
  public void delete(DeskDto deskDto) throws GenericServiceException {
    List<BookingDto> bookingDtos = bookingController.bookings();
    for (BookingDto booking : bookingDtos) {
      if (booking.getDesk().getDeskId().equals(deskDto.getDeskId())) {
        bookingController.delete(booking);
      }
    }
    deskService.setDeletedFlag(deskService.findById(deskDto.getDeskId()), true);
  }

  @Override
  public void restore(DeskDto deskDto) throws GenericServiceException {
    deskService.setDeletedFlag(
      deskService.findById(deskDto.getDeskId()),
      false
    );
  }
}
