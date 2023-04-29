package flexDesk.javafx.controller;

import flexDesk.api.controller.GebaeudeController;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AddBuildingViewController {

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
  private ConfigurableApplicationContext springContext;

  OverallSettingsViewController overallSettingsViewController;

  private Stage stage;

  public void button_add_click() throws IOException, GenericServiceException {
    String building = textfield_name.getText();
    if (building.equals("")) {
      label_msg.setTextFill(Color.RED);
      label_msg.setText("Gebäude sollte einen Namen haben");
    } else {
      gebaeudeController.create(building);
      overallSettingsViewController.init();
      button_cancel_click();
    }
  }

  public void button_cancel_click() {
    Stage stage = (Stage) label_msg.getScene().getWindow();
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
