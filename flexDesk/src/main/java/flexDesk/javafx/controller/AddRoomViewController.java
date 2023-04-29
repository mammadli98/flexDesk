package flexDesk.javafx.controller;

import flexDesk.api.contract.GebaeudeDto;
import flexDesk.api.controller.GebaeudeController;
import flexDesk.api.controller.RaumController;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AddRoomViewController {

  @FXML
  MenuButton menubutton_buildings;

  @FXML
  TextField textfield_name;

  @FXML
  Button button_add;

  @FXML
  Button button_cancel;

  @FXML
  Label label_msg;

  @Autowired
  GebaeudeController gebaeudeController;

  @Autowired
  RaumController raumController;

  @Autowired
  private ConfigurableApplicationContext springContext;

  OverallSettingsViewController overallSettingsViewController;

  GebaeudeDto building;

  private Stage stage;

  public void init() {
    List<GebaeudeDto> buildings = gebaeudeController.gebaeude();
    for (GebaeudeDto building : buildings) {
      MenuItem gebaeude = new MenuItem(building.getGebaeudeName());
      gebaeude.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            choose_building(building);
          }
        }
      );
      menubutton_buildings.getItems().add(gebaeude);
    }
  }

  public void button_add_click() throws IOException, GenericServiceException {
    String room = textfield_name.getText();
    if (room.equals("")) {
      label_msg.setTextFill(Color.RED);
      label_msg.setText("Raum sollte einen Namen haben");
    } else if (building == null) {
      label_msg.setTextFill(Color.RED);
      label_msg.setText("Gebäude erforderlich");
    } else {
      raumController.create(this.building, room);
      overallSettingsViewController.init();
      button_cancel_click();
    }
  }

  public void choose_building(GebaeudeDto gebaeude) {
    this.building = gebaeude;
    menubutton_buildings.setText(gebaeude.getGebaeudeName());
  }

  public void button_cancel_click() {
    Stage stage = (Stage) label_msg.getScene().getWindow();
    overallSettingsViewController.init();
    stage.close();
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {}

  public void setOverallSettingsViewController(
    OverallSettingsViewController overallSettingsViewController
  ) {
    this.overallSettingsViewController = overallSettingsViewController;
  }
}
