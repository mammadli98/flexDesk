package flexDesk.javafx.controller;

import flexDesk.api.controller.UserController;
import flexDesk.backend.entities.derivatedAttributes.Group;
import flexDesk.tools.GlobalSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RegisterViewController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  @Autowired
  private UserController userController;

  @FXML
  TextField TextField_username;

  @FXML
  TextField TextField_password;

  @FXML
  TextField TextField_repeat_password;

  @FXML
  Button Button_register;

  @FXML
  Label Label_back;

  @FXML
  Label Label_msg;

  private FXMLLoader fxmlLoader;
  private Stage stage;

  @FXML
  public void Button_register_click(ActionEvent event) {
    int minpwdlength = GlobalSettings.getInstance().getMinimalPasswordLength();
    if (userController.checkExistingUsername(TextField_username.getText())) {
      Label_msg.setTextFill(Color.color(1, 0, 0));
      setText("Nutzer bereits hinzugefügt.");
    } else if (TextField_username.getText().equals("")) {
      setText("Nutzername erforderlich.");
    } else if (TextField_password.getText().equals("")) {
      setText("Passwort erforderlich");
    } else if (TextField_password.getText().length() < minpwdlength) {
      setText(
        "Passwort sollte mindestens " + minpwdlength + " Zeichen lang sein"
      );
    } else if (TextField_repeat_password.getText().equals("")) {
      setText("Passwort wurde nicht wiederholt.");
    } else if (
      !TextField_password.getText().equals(TextField_repeat_password.getText())
    ) {
      setText("Wiederholtes Passwort stimmt nicht überein.");
    } else {
      userController.create(
        TextField_username.getText(),
        TextField_password.getText(),
        Group.USER,
        false
      );
      Stage stage = (Stage) Label_back.getScene().getWindow();
      stage.close();
    }
  }

  private void setText(String msg) {
    Label_msg.setText(msg);
  }

  public void Label_back_click() {
    Stage stage = (Stage) Label_back.getScene().getWindow();
    stage.close();
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {
    this.springContext = springContext;
  }
}
