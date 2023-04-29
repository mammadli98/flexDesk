package flexDesk.javafx.controller;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.shape.StrokeType.OUTSIDE;

import flexDesk.api.contract.BookingDto;
import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.FeatureDto;
import flexDesk.api.contract.UserDto;
import flexDesk.api.controller.BookingController;
import flexDesk.api.controller.DeskController;
import flexDesk.api.controller.FeatureController;
import flexDesk.api.controller.MultiDeskBookingController;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MultideskBookingViewController {

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
  Label label_error;

  @FXML
  VBox vbox_features;

  @FXML
  ImageView roomImageView;

  @FXML
  Text textNoImage;

  @FXML
  VBox vboxDesks;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private FeatureController featureController;

  @Autowired
  private DeskController deskController;

  @Autowired
  private MultiDeskBookingController multiDeskBookingController;

  @Autowired
  private ConfigurableApplicationContext springContext;

  private UserDto user;

  private DeskDto currentlySelectedDesk;

  private List<DeskDto> mdbDeskList = new ArrayList<>();

  private LocalDate localDate;
  private TimePeriod timePeriod;

  public void init() {
    vbox_features.getChildren().clear();
    vboxDesks.getChildren().clear();
    this.user = mainViewController.getUser();
    this.localDate = mainViewController.getFilterDate();
    this.timePeriod = mainViewController.getMdbPeriod();
    addDesksToView();
  }

  public void addDesksToView() {
    List<DeskDto> deleted = deskController.deleted_desks();
    for (DeskDto desk : mdbDeskList) {
      if (!deleted.contains(desk)) {
        AnchorPane tisch = add_Desk_graphic(desk);
        vboxDesks.getChildren().add(tisch);
      }
    }
  }

  public AnchorPane add_Desk_graphic(DeskDto deskToDisplay) {
    AnchorPane anchorPaneGraphicBooking = new AnchorPane();
    anchorPaneGraphicBooking.setMinHeight(36.0);
    anchorPaneGraphicBooking.setPrefHeight(36.0);
    anchorPaneGraphicBooking.setMaxHeight(36.0);
    anchorPaneGraphicBooking.setMinWidth(300.0);

    anchorPaneGraphicBooking.setStyle(
      "-fx-background-color: #bfcbd4; -fx-background-insets: 2;"
    );

    // Adding child to parent
    Text textDeskName = new Text();
    textDeskName.setStrokeWidth(0.0);
    textDeskName.setStrokeType(OUTSIDE);
    textDeskName.setLayoutX(24.0);
    textDeskName.setLayoutY(24.0);
    textDeskName.setText(deskToDisplay.getName());
    textDeskName.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textDeskName);
    anchorPaneGraphicBooking.setLeftAnchor(textDeskName, 24.0);

    // Adding child to parent
    Text textRoom = new Text();
    textRoom.setStrokeWidth(0.0);
    textRoom.setStrokeType(OUTSIDE);
    textRoom.setLayoutX(110.0);
    textRoom.setLayoutY(24.0);
    textRoom.setText(deskToDisplay.getRaum().getRaumName());
    textRoom.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textRoom);
    anchorPaneGraphicBooking.setLeftAnchor(textRoom, 110.0);

    // Adding child to parent
    Text textBuilding = new Text();
    textBuilding.setStrokeWidth(0.0);
    textBuilding.setStrokeType(OUTSIDE);
    textBuilding.setLayoutX(200.0);
    textBuilding.setLayoutY(24.0);
    textBuilding.setText(deskToDisplay.getGebaeude().getGebaeudeName());
    anchorPaneGraphicBooking.getChildren().add(textBuilding);
    anchorPaneGraphicBooking.setLeftAnchor(textBuilding, 250.0);
    anchorPaneGraphicBooking.setCursor(Cursor.HAND);

    anchorPaneGraphicBooking.setOnMouseClicked(
      event -> {
        anchorPaneGraphicBookingClick(deskToDisplay, anchorPaneGraphicBooking);
      }
    );

    return anchorPaneGraphicBooking;
  }

  public void anchorPaneGraphicBookingClick(
    DeskDto desk,
    AnchorPane anchorPane
  ) {
    text_building.setText(desk.getGebaeude().getGebaeudeName());
    text_room.setText(desk.getName());
    text_InetSpeed.setText("" + desk.getInternetGeschwindigkeit());
    List<FeatureDto> features = desk.getFeatures();
    vbox_features.getChildren().clear();
    for (FeatureDto feature : features) {
      Text platzhalter = new Text();
      Text text = new Text(feature.getFeatureName());
      vbox_features.getChildren().add(text);
      vbox_features.getChildren().add(platzhalter);
      roomImageView.setImage(null);
      setRoomImage("/roomPlanImages/" + desk.getRaum().getRaumName() + ".png");
    }
  }

  private void setRoomImage(String roomImageUrl) {
    try {
      Image image = new Image(roomImageUrl);
      roomImageView.setImage(image);
      roomImageView.setVisible(true);
    } catch (IllegalArgumentException e) {
      //textNoImage.setVisible(true);
    }
  }

  public void button_confirm_click(ActionEvent event)
    throws GenericServiceException, IOException {
    List<BookingDto> bookings = new ArrayList<>();
    for (DeskDto desk : mdbDeskList) {
      bookings.add(
        bookingController.create(
          user,
          desk,
          this.localDate,
          this.timePeriod,
          true
        )
      );
    }
    multiDeskBookingController.create(
      user,
      this.localDate,
      this.timePeriod,
      bookings
    );
    mainViewController.setMultideskBookingActivated(false);
    mainViewController.mdbs.removeAll(mainViewController.mdbs);
    mainViewController.buttonMD.setText("Multidesk Modus");
    mainViewController.init();
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void button_cancel_click(ActionEvent event) throws IOException {
    mainViewController.mdbs.removeAll(mainViewController.mdbs);
    mainViewController.init();
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
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

  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }

  public MainViewController getMainViewController() {
    return mainViewController;
  }

  public void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }

  public List<DeskDto> getMdbDeskList() {
    return mdbDeskList;
  }

  public void setMdbDeskList(List<DeskDto> mdbDeskList) {
    this.mdbDeskList = mdbDeskList;
  }
}
