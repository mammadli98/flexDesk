package flexDesk.api;

import flexDesk.api.contract.*;
import flexDesk.backend.entities.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

  public GebaeudeDto gebaeudeToGebaeudeDto(Gebaeude gebaeude) {
    return new GebaeudeDto(
      gebaeude.getGebaeudeId(),
      gebaeude.getGebaeudeName()
    );
  }

  public List<GebaeudeDto> gebaeudeToGebaeudeDtos(List<Gebaeude> gebaeudeList) {
    return gebaeudeList
      .stream()
      .map(gebaeude -> gebaeudeToGebaeudeDto(gebaeude))
      .collect(Collectors.toList());
  }

  public RaumDto raumToRaumDto(Raum raum) {
    return new RaumDto(
      raum.getRaumId(),
      gebaeudeToGebaeudeDto(raum.getGebaeude()),
      raum.getRaumName(),
      raum.isDeletedFlag()
    );
  }

  public List<RaumDto> roomsToRoomDtos(List<Raum> rooms) {
    return rooms
      .stream()
      .map(raum -> raumToRaumDto(raum))
      .collect(Collectors.toList());
  }

  public DeskDto deskToDeskDto(Desk desk) {
    return new DeskDto(
      desk.getDeskId(),
      raumToRaumDto(desk.getRaum()),
      gebaeudeToGebaeudeDto(desk.getGebaeude()),
      desk.getName(),
      desk.getInternetGeschwindigkeit(),
      featuresToFeatureDtos(desk.getFeatures()),
      desk.getPopularityScore(),
      desk.isDeletedFlag()
    );
  }

  public List<DeskDto> desksToDeskDtos(List<Desk> desks) {
    return desks
      .stream()
      .sorted(
        Comparator.comparing(
          Desk::getPopularityScore,
          Comparator.reverseOrder()
        )
      )
      .map(desk -> deskToDeskDto(desk))
      .collect(Collectors.toList());
  }

  public UserDto userToUserDto(User user) {
    return new UserDto(
      user.getUserId(),
      user.getUserName(),
      user.getPasswordHash(),
      user.getGroupMember(),
      user.isProjectLead(),
      user.isDeletedFlag()
    );
  }

  public FeatureDto featureToFeatureDto(Feature feature) {
    return new FeatureDto(feature.getFeatureId(), feature.getFeatureName());
  }

  public List<FeatureDto> featuresToFeatureDtos(List<Feature> features) {
    return features
      .stream()
      .map(feature -> featureToFeatureDto(feature))
      .collect(Collectors.toList());
  }

  public BookingDto bookingToBookingDto(Booking booking) {
    return new BookingDto(
      booking.getBookingId(),
      booking.isMdb(),
      booking.getBookingDate(),
      booking.getBookingPeriod(),
      userToUserDto(booking.getUser()),
      deskToDeskDto(booking.getDesk()),
      booking.isDeletedFlag()
    );
  }

  public List<BookingDto> bookingsToBookingDtos(List<Booking> bookings) {
    return bookings
      .stream()
      .map(booking -> bookingToBookingDto(booking))
      .collect(Collectors.toList());
  }

  public List<UserDto> usersToUserDtos(List<User> users) {
    return users
      .stream()
      .map(user -> userToUserDto(user))
      .collect(Collectors.toList());
  }

  public MultiDeskBookingDto mdbToMdbDto(MultiDeskBooking multiDeskBooking) {
    return new MultiDeskBookingDto(
      multiDeskBooking.getMdbId(),
      multiDeskBooking.getDate(),
      multiDeskBooking.getTimePeriod(),
      userToUserDto(multiDeskBooking.getUser()),
      bookingsToBookingDtos(multiDeskBooking.getBookingList()),
      multiDeskBooking.isDeletedFlag()
    );
  }

  public List<MultiDeskBookingDto> mdbsToMdbDtos(
    List<MultiDeskBooking> bookingList
  ) {
    return bookingList
      .stream()
      .map(mdb -> mdbToMdbDto(mdb))
      .collect(Collectors.toList());
  }
}
