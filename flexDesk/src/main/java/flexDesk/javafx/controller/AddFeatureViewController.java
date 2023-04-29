package flexDesk.javafx.controller;

import flexDesk.api.contract.FeatureDto;
import flexDesk.api.controller.FeatureController;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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
public class AddFeatureViewController {

  @FXML
  TextField textfield_feature;

  @FXML
  Button button_add;

  @FXML
  Button button_cancel;

  @FXML
  Label label_msg;

  @Autowired
  FeatureController featureController;

  private List<String> existingFeatures;

  @Autowired
  private ConfigurableApplicationContext springContext;

  AdminViewController adminViewController;

  private Stage stage;

  public AddFeatureViewController() {}

  @FXML
  public void initialize() {
    existingFeatures =
      featureController
        .features()
        .stream()
        .map(FeatureDto::getFeatureName)
        .collect(Collectors.toList());
  }

  public void button_add_click() throws IOException {
    String featurename = textfield_feature.getText();
    if (featurename.equals("")) {
      label_msg.setTextFill(Color.RED);
      label_msg.setText("Feature sollte einen Namen haben");
    } else if (existingFeatures.contains(featurename)) {
      label_msg.setTextFill(Color.RED);
      label_msg.setText("Feature existiert bereits");
    } else {
      featureController.create(featurename);
      adminViewController.init();
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

  public void setAdminViewController(AdminViewController adminViewController) {
    this.adminViewController = adminViewController;
  }
}
