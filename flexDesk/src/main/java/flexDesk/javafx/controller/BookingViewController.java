package flexDesk.javafx.controller;

import flexDesk.api.contract.BookingDto;
import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.FeatureDto;
import flexDesk.api.contract.UserDto;
import flexDesk.api.controller.BookingController;
import flexDesk.api.controller.FeatureController;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.services.exceptions.GenericServiceException;
import flexDesk.tools.DateFormat;
import flexDesk.tools.GlobalSettings;
import flexDesk.tools.MainViewGraphicElementUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BookingViewController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  private UserDto user;

  private DeskDto desk;

  private LocalDate localDate;

  private TimePeriod timePeriod;

  private String roomImageUrl;

  @FXML
  Button button_confirm;

  @FXML
  Button button_cancel;

  @FXML
  Text text_building;

  @FXML
  Text text_room;

  @FXML
  Text text_InetSpeed;

  @FXML
  DatePicker datePicker;

  @FXML
  MenuButton menu_bookingPeriod;

  @FXML
  Label label_error;

  @FXML
  VBox vbox_features;

  @FXML
  ImageView roomImageView;

  @FXML
  Text textNoImage;

  public MainViewController getMainViewController() {
    return mainViewController;
  }

  public void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private FeatureController featureController;

  public void init() {
    this.roomImageUrl =
      "/roomPlanImages/" + this.desk.getRaum().getRaumName() + ".png";
    this.setRoomImage();
    this.localDate = mainViewController.getFilterDate();
    datePicker.setPromptText(DateFormat.formatDate(this.localDate));
    checkDateAvailability(this.localDate);
    List<FeatureDto> features = this.getDesk().getFeatures();
    for (FeatureDto ft : features) {
      Text feature_name = new Text("-" + ft.getFeatureName());
      feature_name.setFont(Font.font(18));
      vbox_features.getChildren().add(feature_name);
    }
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
      public DateCell call(final DatePicker datePicker) {
        return new DateCell() {
          @Override
          public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            setDisable(
              empty ||
              item.isBefore(LocalDate.now()) ||
              item.isAfter(
                LocalDate
                  .now()
                  .plusDays(
                    GlobalSettings.getInstance().getBookingTimeFrameLenght()
                  )
              )
            );

            if (
              !(
                empty ||
                item.isBefore(LocalDate.now()) ||
                item.isAfter(
                  LocalDate
                    .now()
                    .plusDays(
                      GlobalSettings.getInstance().getBookingTimeFrameLenght()
                    )
                )
              )
            ) {
              List<TimePeriod> periods = bookingController
                .findByDate(item)
                .stream()
                .filter(
                  bookingDto ->
                    bookingDto.getDesk().getDeskId().equals(desk.getDeskId())
                )
                .map(BookingDto::getBookingPeriod)
                .collect(Collectors.toList());

              if (
                periods.contains(TimePeriod.FULLDAY) ||
                (
                  periods.contains(TimePeriod.AFTERNOON) &&
                  periods.contains(TimePeriod.MORNING)
                )
              ) {
                setDisable(true);
                setStyle("-fx-background-color: #b94646;");
              } else if (periods.contains(TimePeriod.AFTERNOON)) {
                setStyle("-fx-background-color: #ffcc85;");
              } else if (periods.contains(TimePeriod.MORNING)) {
                setStyle("-fx-background-color: #aca6ff;");
              } else {
                setStyle("-fx-background-color: #8ff5c2;");
              }
            }
          }
        };
      }
    };
    this.datePicker.setDayCellFactory(dayCellFactory);
    this.datePicker.setOnAction(
        event -> {
          this.localDate = datePicker.getValue();
          this.checkDateAvailability(this.localDate);
        }
      );
    this.datePicker.setConverter(DateFormat.getConverter());
  }

  private void setRoomImage() {
    try {
      Image image = new Image(this.roomImageUrl);
      roomImageView.setImage(image);
      roomImageView.setVisible(true);
    } catch (IllegalArgumentException e) {
      textNoImage.setVisible(true);
    }
  }

  public void checkDateAvailability(LocalDate localDate) {
    button_confirm.setDisable(false);

    List<TimePeriod> periods = bookingController
      .findByDate(localDate)
      .stream()
      .filter(
        bookingDto -> bookingDto.getDesk().getDeskId().equals(desk.getDeskId())
      )
      .map(BookingDto::getBookingPeriod)
      .collect(Collectors.toList());

    if (
      periods.contains(TimePeriod.AFTERNOON) &&
      periods.contains(TimePeriod.MORNING)
    ) {
      menu_bookingPeriod.setDisable(true);
      menu_bookingPeriod.setText("Nicht buchbar");
      button_confirm.setDisable(true);
    } else if (periods.contains(TimePeriod.MORNING)) {
      menu_bookingPeriod.setDisable(true);
      menu_bookingPeriod.setText("Nachmittags");
      timePeriod = TimePeriod.AFTERNOON;
    } else if (periods.contains(TimePeriod.AFTERNOON)) {
      menu_bookingPeriod.setDisable(true);
      menu_bookingPeriod.setText("Vormittags");
      timePeriod = TimePeriod.MORNING;
    } else {
      menu_bookingPeriod.setDisable(false);
      menu_bookingPeriod.setText("Ganzer Tag");
      timePeriod = TimePeriod.FULLDAY;
    }
  }

  public void button_confirm_click(ActionEvent event)
    throws GenericServiceException {
    MainViewGraphicElementUtil.AddElementToView(
      bookingController.create(
        this.getUser(),
        this.getDesk(),
        this.getLocalDate(),
        this.timePeriod,
        false
      ),
      mainViewController
    );

    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void button_cancel_click(ActionEvent event) throws IOException {
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void choose_fullDay() {
    menu_bookingPeriod.setText("Ganzer Tag");
    timePeriod = TimePeriod.FULLDAY;
  }

  public void choose_morning() {
    menu_bookingPeriod.setText("Vormittags");
    timePeriod = TimePeriod.MORNING;
  }

  public void choose_afternoon() {
    menu_bookingPeriod.setText("Nachmittags");
    timePeriod = TimePeriod.AFTERNOON;
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {}

  public void setText_building(String text_building) {
    this.text_building.setText(text_building);
  }

  public void setText_room(String text_room) {
    this.text_room.setText(text_room);
  }

  public void setText_InetSpeed(Float text_InetSpeed) {
    this.text_InetSpeed.setText(text_InetSpeed.toString() + " MB/s");
  }

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }

  public DeskDto getDesk() {
    return desk;
  }

  public void setDesk(DeskDto desk) {
    this.desk = desk;
  }

  public void pick_date() {
    this.setLocalDate(datePicker.getValue());
  }

  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }
}
