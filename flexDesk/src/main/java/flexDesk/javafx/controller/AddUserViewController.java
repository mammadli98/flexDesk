package flexDesk.javafx.controller;

import flexDesk.api.controller.UserController;
import flexDesk.backend.entities.derivatedAttributes.Group;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AddUserViewController {

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
  Button button_add;

  @FXML
  Label Label_back;

  @FXML
  MenuButton menubutton_role;

  @FXML
  MenuItem role_user;

  @FXML
  MenuItem role_accountant;

  @FXML
  MenuItem role_admin;

  @FXML
  CheckBox checkbox_projectlead;

  @FXML
  Label Label_msg;

  String username;
  String password;
  Group group;
  boolean projectlead;

  OverallSettingsViewController overallSettingsViewController;

  private FXMLLoader fxmlLoader;
  private Stage stage;

  @FXML
  public void button_add_click() throws IOException {
    choose_projectlead();
    choose_username();
    choose_password();
    if (this.username.equals("")) {
      setText("Nutzername erforderlich");
    } else if (userController.checkExistingUsername(username)) {
      setText("Name bereits vergeben");
    } else if (this.password.equals("")) {
      setText("Passwort erforderlich");
    } else if (this.group == null) {
      this.group = Group.USER; //as default value
    } else if (
      userController.checkExistingUsername(TextField_username.getText())
    ) {
      Label_msg.setTextFill(Color.color(1, 0, 0));
      setText("Nutzer bereits hinzugefügt.");
    } else {
      userController.create(username, password, group, projectlead);
      Stage stage = (Stage) Label_back.getScene().getWindow();
      overallSettingsViewController.init();
      stage.close();
    }
  }

  public void choose_group_admin() {
    this.group = null;
    menubutton_role.setText("Admin");
    this.group = Group.ADMIN;
  }

  public void choose_group_user() {
    this.group = null;
    menubutton_role.setText("User");
    this.group = Group.USER;
  }

  public void choose_group_accountant() {
    this.group = null;
    menubutton_role.setText("Accountant");
    this.group = Group.ACCOUNTANT;
  }

  public void choose_projectlead() {
    this.projectlead = checkbox_projectlead.isSelected();
  }

  public void choose_password() {
    if (
      !TextField_password.getText().equals(TextField_repeat_password.getText())
    ) {
      setText("Wiederholtes Passwort stimmt nicht überein");
    } else {
      this.password = TextField_password.getText();
    }
  }

  public void choose_username() {
    this.username = TextField_username.getText();
  }

  private void setText(String msg) {
    Label_msg.setText(msg);
  }

  public void Label_back_click() {
    Stage stage = (Stage) Label_back.getScene().getWindow();
    stage.close();
  }

  public void setOverallSettingsViewController(
    OverallSettingsViewController overallSettingsViewController
  ) {
    this.overallSettingsViewController = overallSettingsViewController;
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {
    this.springContext = springContext;
  }
}
