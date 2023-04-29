package flexDesk.javafx.controller;

import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.RaumDto;
import flexDesk.api.contract.UserDto;
import flexDesk.api.controller.DeskController;
import flexDesk.api.controller.GebaeudeController;
import flexDesk.api.controller.RaumController;
import flexDesk.api.controller.UserController;
import flexDesk.backend.services.exceptions.GenericServiceException;
import flexDesk.tools.GlobalSettings;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class OverallSettingsViewController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  @Autowired
  private DeskController deskController;

  @Autowired
  GebaeudeController gebaeudeController;

  @Autowired
  RaumController raumController;

  @Autowired
  UserController userController;

  @FXML
  VBox vbox_rooms;

  @FXML
  VBox vbox_users;

  @FXML
  VBox vbox_deleted_desks;

  @FXML
  Button button_add_room;

  @FXML
  Button button_add_user;

  @FXML
  Button button_statistics;

  @FXML
  Button button_mainMenu;

  @FXML
  Button button_logout;

  @FXML
  Button button_admin;

  @FXML
  TextField textfield_months;

  @FXML
  TextField textfield_weeks;

  @FXML
  TextField textfield_days;

  @FXML
  TextField textfield_pwdlength;

  @FXML
  CheckBox checkbox_parentalmode;

  @FXML
  Button button_save_period;

  @FXML
  Button button_save_pwdlength;

  @FXML
  Button button_save_parentalmode;

  @FXML
  Text text_msg;

  private Stage stage;
  private Scene scene;

  GlobalSettings globalSettings = GlobalSettings.getInstance();
  private FXMLLoader fxmlLoader;

  public void init() {
    vbox_rooms.getChildren().clear();
    vbox_deleted_desks.getChildren().clear();
    vbox_users.getChildren().clear();

    add_users_to_view();
    add_deleted_desks_to_view();
    add_rooms_to_view();
    load_global_settings();
  }

  public void load_global_settings() {
    setText("", Color.BLACK);
    textfield_pwdlength.setText("" + globalSettings.getMinimalPasswordLength());
    if (globalSettings.isParentalModeSetting()) {
      checkbox_parentalmode.setSelected(true);
    }
    textfield_months.setText("" + globalSettings.getMonths());
    textfield_weeks.setText("" + globalSettings.getWeeks());
    textfield_days.setText("" + globalSettings.getDays());
  }

  public void add_users_to_view() {
    List<UserDto> users = userController.users();
    for (UserDto user : users) {
      Text username = new Text(
        "Nutzername: " +
        user.getUserName() +
        "  Rolle: " +
        user.getGroupMember().toString().toLowerCase()
      );
      Text placeholder = new Text();
      vbox_users.getChildren().add(username);
      vbox_users.getChildren().add(placeholder);
    }
  }

  public void add_deleted_desks_to_view() {
    List<DeskDto> desks = deskController.deleted_desks();
    for (DeskDto desk : desks) {
      HBox deleted = new HBox();
      Button button_restore = new Button("Wiederherstellen");
      button_restore.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            try {
              button_restore_click(event, desk);
            } catch (GenericServiceException e) {
              System.err.println(e.getCause() + e.getMessage());
            }
          }
        }
      );

      Text name = new Text(desk.getName());
      deleted.getChildren().add(name);
      deleted.getChildren().add(button_restore);
      deleted.setMargin(button_restore, new Insets(0, 0, 0, 10));
      deleted.setMargin(name, new Insets(0, 10, 0, 0));
      vbox_deleted_desks.getChildren().add(deleted);
    }
  }

  public void add_rooms_to_view() {
    List<RaumDto> rooms = raumController.rooms();
    for (RaumDto raum : rooms) {
      Text room = new Text(raum.getRaumName());
      Text placeholder = new Text();
      vbox_rooms.getChildren().add(room);
      vbox_rooms.getChildren().add(placeholder);
    }
  }

  public void button_restore_click(ActionEvent event, DeskDto desk)
    throws GenericServiceException {
    deskController.restore(desk);
    init();
  }

  public void button_add_room_click() throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/AddRoomView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    AddRoomViewController addRoomViewController = fxmlLoader.getController();
    addRoomViewController.setSpringContext(springContext);
    addRoomViewController.init();
    addRoomViewController.setOverallSettingsViewController(this);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Raum hinzufügen");
    stage2.show();
  }

  public void button_add_user_click() throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/AddUserView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    AddUserViewController addUserViewController = fxmlLoader.getController();
    addUserViewController.setSpringContext(springContext);
    addUserViewController.setOverallSettingsViewController(this);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Nutzer hinzufügen");
    stage2.show();
  }

  public void button_save_period_click() {
    try {
      int months = Integer.parseInt(textfield_months.getText());
      int weeks = Integer.parseInt(textfield_weeks.getText());
      int days = Integer.parseInt(textfield_days.getText());
      int sum = months * 30 + weeks * 7 + days;
      globalSettings.setBookingTimeFrameLenght(sum);
      globalSettings.setMonths(months);
      globalSettings.setWeeks(weeks);
      globalSettings.setDays(days);
      setText("Zeitfenster gespeichert", Color.GREEN);
    } catch (NumberFormatException nfe) {
      setText("Zeitrahmen sollte in einer Zahl angegeben werden.", Color.RED);
    }
  }

  public void button_save_pwdlength_click() {
    if (textfield_pwdlength.getText().equals("")) {
      setText("Minimale Passwortlänge sollte nicht 0 sein", Color.RED);
    } else {
      try {
        int pwdlength = Integer.parseInt(textfield_pwdlength.getText());
        globalSettings.setMinimalPasswordLength(pwdlength);
        textfield_pwdlength.setText("" + pwdlength);
        setText("Passwortlänge auf " + pwdlength + " gesetzt.", Color.GREEN);
      } catch (NumberFormatException nfe) {
        setText("Passwortlänge sollte eine Zahl sein.", Color.RED);
        textfield_pwdlength.setText(
          "" + globalSettings.getMinimalPasswordLength()
        );
      }
    }
  }

  public void button_save_parentalmode_click() {
    if (checkbox_parentalmode.isSelected()) {
      globalSettings.setParentalModeSetting(true);
      setText("Parental mode aktiviert.", Color.GREEN);
    } else if (!checkbox_parentalmode.isSelected()) {
      globalSettings.setParentalModeSetting(false);
      setText("Parental mode deaktiviert.", Color.GREEN);
    }
  }

  public void button_statistics_click() {
    //unfinished feature
  }

  public void button_mainView_click() throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    MainViewController mainViewController = fxmlLoader.getController();
    mainViewController.setSpringContext(springContext);
    mainViewController.init();
    stage = (Stage) button_mainMenu.getScene().getWindow();
    stage.getScene().setRoot(root);
    stage.show();
  }

  public void button_logout_click() throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/LoginView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    LoginViewController loginViewController = fxmlLoader.getController();
    loginViewController.setSpringContext(springContext);
    stage = (Stage) button_mainMenu.getScene().getWindow();
    stage.getScene().setRoot(root);
    stage.show();
  }

  public void button_admin_click() throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/AdminView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    AdminViewController adminViewController = fxmlLoader.getController();
    adminViewController.setSpringContext(springContext);
    adminViewController.init();
    stage = (Stage) button_admin.getScene().getWindow();
    stage.getScene().setRoot(root);
    stage.show();
  }

  private void setText(String text, Color color) {
    text_msg.setText(text);
    text_msg.setFill(color);
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {}

  //Helper function
  public void setParentalMode(boolean setting) {
    GlobalSettings.getInstance().setParentalModeSetting(setting); //true is on, false is of
  }
}
