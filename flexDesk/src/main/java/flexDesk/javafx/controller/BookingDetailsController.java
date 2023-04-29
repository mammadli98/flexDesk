package flexDesk.javafx.controller;

import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.FeatureDto;
import flexDesk.api.contract.UserDto;
import flexDesk.api.controller.FeatureController;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import java.time.LocalDate;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BookingDetailsController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  private UserDto user;

  private DeskDto desk;

  private LocalDate localDate;

  private TimePeriod timePeriod;

  @FXML
  Text text_building;

  @FXML
  Text text_room;

  @FXML
  Text text_InetSpeed;

  @FXML
  VBox vbox_features;

  @FXML
  Text textDate;

  @FXML
  Text textTimePeriod;

  public MainViewController getMainViewController() {
    return mainViewController;
  }

  public void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private FeatureController feautureController;

  public void init() {
    List<FeatureDto> features = this.getDesk().getFeatures();

    for (FeatureDto ft : features) {
      Text feature_name = new Text(ft.getFeatureName());
      feature_name.setFont(Font.font(18));
      vbox_features.getChildren().add(feature_name);
    }
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

  public void setTextDate(String textDate) {
    this.textDate.setText(textDate);
  }

  public void setTextTimePeriod(String textTimePeriod) {
    this.textTimePeriod.setText(textTimePeriod);
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
}
