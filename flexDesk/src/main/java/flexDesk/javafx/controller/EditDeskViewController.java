package flexDesk.javafx.controller;

import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.FeatureDto;
import flexDesk.api.contract.GebaeudeDto;
import flexDesk.api.contract.RaumDto;
import flexDesk.api.controller.DeskController;
import flexDesk.api.controller.FeatureController;
import flexDesk.api.controller.GebaeudeController;
import flexDesk.api.controller.RaumController;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.io.IOException;
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
public class EditDeskViewController {

  @FXML
  MenuButton MenuButton_building;

  @FXML
  MenuButton MenuButton_room;

  @FXML
  VBox vbox_features;

  @FXML
  Button button_confirm;

  @FXML
  Button button_cancel;

  @FXML
  TextField textfield_ispeed;

  @FXML
  TextField textfield_name;

  @FXML
  Label label_msg;

  @FXML
  MenuButton MenuButton_features;

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

  private Stage stage;
  private boolean buildingChangedFlag = false;
  private AdminViewController adminViewController;

  GebaeudeDto gebaeudeDto;
  RaumDto raumDto;
  DeskDto desktoedit;
  Float ispeed;
  String deskname;
  List<FeatureDto> deskFeatures;

  private List<RaumDto> rooms;
  private List<GebaeudeDto> buildings;

  public void init() {
    MenuButton_building.setVisible(false);
    MenuButton_building.setText(desktoedit.getGebaeude().getGebaeudeName());
    MenuButton_room.setText(desktoedit.getRaum().getRaumName());
    raumDto = desktoedit.getRaum();
    deskFeatures = desktoedit.getFeatures();
    textfield_ispeed.setText("" + desktoedit.getInternetGeschwindigkeit());
    textfield_name.setText(desktoedit.getName());
    MenuButton_building.getItems().clear();
    MenuButton_room.getItems().clear();
    MenuButton_features.getItems().clear();
    add_buildings_to_view();
    add_rooms_to_view();
    add_desk_features_to_view(); //all features the desk has
    add_features_to_button(); //all available features
  }

  public void button_confirm_click()
    throws GenericServiceException, IOException {
    if (buildingChangedFlag && raumDto == null) {
      label_msg.setText("Bitte neuen Raum wählen");
      label_msg.setTextFill(Color.color(1, 0, 0));
      return;
    } else if (textfield_name.getText().equals("")) {
      label_msg.setTextFill(Color.color(1, 0, 0));
      label_msg.setText("Tischname erforderlich");
    } else {
      choose_ispeed();
      choose_deskname();
      deskController.edit(desktoedit, raumDto, deskname, ispeed, deskFeatures);
      adminViewController.init();
      stage = (Stage) button_cancel.getScene().getWindow();
      stage.close();
    }
  }

  public void button_cancel_click() throws IOException {
    stage = (Stage) button_cancel.getScene().getWindow();
    adminViewController.init();
    stage.close();
  }

  private void add_buildings_to_view() {
    List<GebaeudeDto> buildings = gebaeudeController.gebaeude();
    for (GebaeudeDto gebaeude : buildings) {
      MenuItem gebaeudetoadd = new MenuItem();
      gebaeudetoadd.setText(gebaeude.getGebaeudeName());
      MenuButton_building.getItems().add(gebaeudetoadd);
      gebaeudetoadd.setOnAction(event -> choose_building(gebaeude));
    }
  }

  private void add_rooms_to_view() {
    //nur räume laden, die im gebäude sind
    MenuButton_building.setText(
      this.desktoedit.getGebaeude().getGebaeudeName()
    );
    gebaeudeDto = this.desktoedit.getGebaeude();
    MenuButton_room.getItems().clear();
    this.rooms =
      raumController
        .rooms()
        .stream()
        .filter(
          raumDto ->
            Objects.equals(
              raumDto.getGebaeude().getGebaeudeId(),
              this.desktoedit.getGebaeude().getGebaeudeId()
            )
        )
        .collect(Collectors.toList());
    for (RaumDto room : rooms) {
      MenuItem raum = new MenuItem();
      raum.setText(room.getRaumName());
      MenuButton_room.getItems().add(raum);
      raum.setOnAction(event -> choose_room(room));
    }
  }

  //loads the feature the desk already has into the view
  private void add_desk_features_to_view() {
    for (FeatureDto feature : deskFeatures) {
      HBox feature_hbox = new HBox();
      feature_hbox.setSpacing(10);
      feature_hbox.setAlignment(Pos.CENTER);

      Text feature_name = new Text(feature.getFeatureName());
      Button feature_remove_button = new Button("Remove");

      feature_remove_button.setOnMouseClicked(
        event -> {
          deskFeatures.remove(feature);
          vbox_features.getChildren().remove(feature_hbox);
        }
      );

      feature_hbox.getChildren().add(feature_name);
      feature_hbox.getChildren().add(feature_remove_button);

      vbox_features.getChildren().add(feature_hbox);
    }
  }

  //Adds all avialable features to menubutton
  private void add_features_to_button() {
    List<FeatureDto> features = feautureController.features();

    features =
      features
        .stream()
        .filter(
          featureDto ->
            !deskFeatures
              .stream()
              .map(FeatureDto::getFeatureId)
              .collect(Collectors.toList())
              .contains(featureDto.getFeatureId())
        )
        .collect(Collectors.toList());

    for (FeatureDto ft : features) {
      MenuItem feature = new MenuItem();
      feature.setText(ft.getFeatureName());
      MenuButton_features.getItems().add(feature);
      feature.setOnAction(event -> choose_feature(ft));
    }
  }

  //adds new features to the right vbox
  private void choose_feature(FeatureDto feature) {
    deskFeatures.add(feature);

    HBox feature_hbox = new HBox();
    feature_hbox.setSpacing(10);
    feature_hbox.setAlignment(Pos.CENTER);

    Text feature_name = new Text(feature.getFeatureName());
    Button feature_remove_button = new Button("Remove");

    feature_remove_button.setOnMouseClicked(
      event -> {
        deskFeatures.remove(feature);
        vbox_features.getChildren().remove(feature_hbox);
      }
    );

    feature_hbox.getChildren().add(feature_name);
    feature_hbox.getChildren().add(feature_remove_button);

    vbox_features.getChildren().add(feature_hbox);
  }

  private void choose_building(GebaeudeDto gebaeude) {
    MenuButton_building.setText(gebaeude.getGebaeudeName());
    gebaeudeDto = gebaeude;
    raumDto = null;
    MenuButton_building.setDisable(true);
    MenuButton_room.getItems().clear();
    this.rooms =
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
    this.MenuButton_room.setText("Raum");
    this.buildingChangedFlag = true;
  }

  private void choose_room(RaumDto raum) {
    MenuButton_room.setText(raum.getRaumName());
    raumDto = raum;
  }

  private void choose_ispeed() {
    String ispeed1 = textfield_ispeed.getText();
    if (ispeed1.contains(",")) {
      label_msg.setTextFill(Color.color(1, 0, 0));
      label_msg.setText("Floats mit '.' schreiben");
    } else if (ispeed1.equals("")) {
      ispeed = desktoedit.getInternetGeschwindigkeit(); //bleibt dann gleich
    } else {
      try {
        ispeed = Float.valueOf(ispeed1);
      } catch (NumberFormatException nfe) {
        label_msg.setTextFill(Color.color(1, 0, 0));
        label_msg.setText("Internetgeschwindigkeit\n sollte eine Zahl sein");
      }
    }
  }

  private void choose_deskname() {
    deskname = textfield_name.getText();
  }

  public void setDesk(DeskDto desk) {
    desktoedit = desk;
  }

  public void setAdminViewController(AdminViewController adminViewController) {
    this.adminViewController = adminViewController;
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {}
}
