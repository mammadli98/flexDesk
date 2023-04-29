package flexDesk.javafx.controller;

import flexDesk.api.contract.UserDto;
import flexDesk.api.controller.UserController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoginViewController {

  @Autowired
  private UserController userController;

  @Autowired
  private ConfigurableApplicationContext springContext;

  @FXML
  Label LABEL01;

  @FXML
  TextField FIELD_USER;

  @FXML
  PasswordField FIELD_PWD;

  @FXML
  Button BUTTON_LOGIN;

  @FXML
  Text Text_register;

  private FXMLLoader fxmlLoader;
  private Parent root;

  @FXML
  protected void button_login_click(ActionEvent event) throws IOException {
    String username = FIELD_USER.getText();
    String passwd = FIELD_PWD.getText();
    if (username.equals("")) {
      LABEL01.setText("Nutzername erforderlich");
    } else if (passwd.equals("")) {
      LABEL01.setText("Passwort erforderlich");
    } else if (userController.checkLoginCredentials(username, passwd)) {
      UserDto loggedInUser = userController.findByUserName(username);
      fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
      fxmlLoader.setControllerFactory(springContext::getBean);
      Parent root = fxmlLoader.load();
      MainViewController mainViewController = fxmlLoader.getController();
      mainViewController.setSpringContext(springContext);
      mainViewController.setUser(loggedInUser);
      mainViewController.init();
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.getScene().setRoot(root);
      stage.show();
    } else {
      LABEL01.setText("Nutzername oder Passwort inkorrekt.");
    }
  }

  public void text_register_click() throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/RegisterView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    RegisterViewController registerViewController = fxmlLoader.getController();
    registerViewController.setSpringContext(springContext);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    Scene scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Register for FlexDesk");
    stage2.show();
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {
    this.springContext = springContext;
  }
}
