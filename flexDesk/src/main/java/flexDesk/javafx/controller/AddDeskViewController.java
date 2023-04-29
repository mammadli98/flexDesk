package flexDesk.javafx.controller;

import flexDesk.api.contract.*;
import flexDesk.api.controller.DeskController;
import flexDesk.api.controller.FeatureController;
import flexDesk.api.controller.GebaeudeController;
import flexDesk.api.controller.RaumController;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AddDeskViewController {

  @FXML
  Label label_msg;

  @FXML
  MenuButton MenuButton_building;

  @FXML
  MenuButton MenuButton_room;

  @FXML
  MenuButton MenuButton_features;

  @FXML
  VBox vbox_features;

  @FXML
  TextField TextField_Ispeed1;

  @FXML
  TextField TextField_name;

  @FXML
  Label Label_abbruch;

  GebaeudeDto Gebaeude;
  RaumDto Raum;
  ArrayList<FeatureDto> features = new ArrayList<>();
  Float Ispeed;
  String name = "";
  UserDto user;

  @Autowired
  private DeskController deskController;

  @Autowired
  private GebaeudeController gebaeudeController;

  @Autowired
  private RaumController raumController;

  @Autowired
  private FeatureController feautureController;

  @Autowired
  private ConfigurableApplicationContext springContext;

  private AdminViewController adminViewController;

  public AdminViewController getAdminViewController() {
    return adminViewController;
  }

  public void setAdminViewController(AdminViewController adminViewController) {
    this.adminViewController = adminViewController;
  }

  private boolean roomOptionUnlock = false;

  @FXML
  public void initialize() {
    MenuButton_building.getItems().clear();
    MenuButton_room.getItems().clear();
    MenuButton_features.getItems().clear();

    //buildings

    List<GebaeudeDto> buildings = gebaeudeController.gebaeude();
    for (GebaeudeDto gebaeude : buildings) {
      MenuItem gebaeudetoadd = new MenuItem();
      gebaeudetoadd.setText(gebaeude.getGebaeudeName());
      MenuButton_building.getItems().add(gebaeudetoadd);
      gebaeudetoadd.setOnAction(
        event -> {
          try {
            choose_building(
              gebaeudeController.gebaeudeIdToGebaeudeDto(
                gebaeude.getGebaeudeId()
              )
            );
          } catch (GenericServiceException e) {
            System.err.println(e.getCause() + e.getMessage());
          }
        }
      );
    }

    //features
    List<FeatureDto> features = feautureController.features();
    for (FeatureDto ft : features) {
      MenuItem feature = new MenuItem();
      feature.setText(ft.getFeatureName());
      MenuButton_features.getItems().add(feature);
      feature.setOnAction(event -> choose_feature(ft));
    }
  }

  public void button_add_desk_click() {
    chooseIspeed();
    choose_name();
    if (name.equals("")) {
      label_msg.setTextFill(Color.RED);
      label_msg.setText("Tischname erforderlich.");
    } else if (Gebaeude == null) {
      label_msg.setTextFill(Color.RED);
      label_msg.setText("Gebäude erforderlich");
    } else if (Raum == null) {
      label_msg.setTextFill(Color.color(1, 0, 0));
      label_msg.setText("Raum erforderlich");
    } else {
      try {
        DeskDto desk = deskController.create(
          Gebaeude,
          Raum,
          name,
          Ispeed,
          features
        );
        this.roomOptionUnlock = false;
        adminViewController.init();
        Stage stage = (Stage) Label_abbruch.getScene().getWindow();
        stage.close();
      } catch (GenericServiceException gse) {
        label_msg.setText("An error occurred. Please try again.");
      } catch (IOException e) {
        System.err.println(e.getCause() + e.getMessage());
      }
    }
  }

  public void choose_building(GebaeudeDto gebaeude) {
    MenuButton_building.setText(gebaeude.getGebaeudeName());
    Gebaeude = gebaeude;
    MenuButton_room.getItems().clear();
    MenuButton_room.setText("Raum");
    List<RaumDto> rooms = null;
    Raum = null;
    rooms =
      raumController
        .rooms()
        .stream()
        .filter(
          raumDto ->
            Objects.equals(
              raumDto.getGebaeude().getGebaeudeId(),
              gebaeude.getGebaeudeId()
            )
        )
        .collect(Collectors.toList());
    for (RaumDto room : rooms) {
      MenuItem raum = new MenuItem();
      raum.setText(room.getRaumName());
      MenuButton_room.getItems().add(raum);
      raum.setOnAction(event -> choose_room(room));
    }
    if (!this.roomOptionUnlock) {
      MenuButton_room.setDisable(false);
      this.roomOptionUnlock = true;
    }
  }

  public void choose_room(RaumDto raum) {
    MenuButton_room.setText(raum.getRaumName());
    Raum = raum;
  }

  public void choose_feature(FeatureDto feature) {
    features.add(feature);

    HBox feature_hbox = new HBox();
    feature_hbox.setSpacing(10);
    feature_hbox.setAlignment(Pos.CENTER);

    Text feature_name = new Text(feature.getFeatureName());
    Button feature_remove_button = new Button("Remove");

    feature_remove_button.setOnMouseClicked(
      event -> {
        features.remove(feature);
        vbox_features.getChildren().remove(feature_hbox);
      }
    );

    feature_hbox.getChildren().add(feature_name);
    feature_hbox.getChildren().add(feature_remove_button);

    vbox_features.getChildren().add(feature_hbox);
  }

  public void chooseIspeed() {
    String ispeed1 = TextField_Ispeed1.getText();
    if (ispeed1.contains(",")) {
      label_msg.setTextFill(Color.color(1, 0, 0));
      label_msg.setText("Floats mit '.' schreiben");
    } else if (ispeed1.equals("")) {
      Ispeed = 0F; //einfach festelegter default wert
    } else {
      try {
        Ispeed = Float.valueOf(ispeed1);
      } catch (NumberFormatException nfe) {
        label_msg.setTextFill(Color.color(1, 0, 0));
        label_msg.setText("Internetgeschwindigkeit\n sollte eine Zahl sein");
      }
    }
  }

  public void choose_name() {
    this.name = TextField_name.getText();
  }

  public void label_abbruch_click() {
    this.roomOptionUnlock = false;
    Stage stage = (Stage) Label_abbruch.getScene().getWindow();
    stage.close();
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {}

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }
}
