package flexDesk.javafx.controller;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.shape.StrokeType.OUTSIDE;

import flexDesk.api.contract.BookingDto;
import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.UserDto;
import flexDesk.api.controller.BookingController;
import flexDesk.api.controller.DeskController;
import flexDesk.backend.services.exceptions.GenericServiceException;
import flexDesk.tools.DateFormat;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The controller for the AdminView.fxml
 */
@Component
public class AdminViewController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  /**
   * Is the currently logged-in user
   */
  private UserDto user;

  @FXML
  Button button_settings;

  @FXML
  Button button_logout;

  @FXML
  Button button_mainMenu;

  @FXML
  Button button_statistics;

  @FXML
  Button add_desk_button;

  @FXML
  VBox vbox_activedesks;

  @FXML
  VBox vbox_desklist;

  @FXML
  Button button_addfeature;

  @Autowired
  private DeskController deskController;

  @Autowired
  private BookingController bookingController;

  private Stage stage;
  private Scene scene;
  private FXMLLoader fxmlLoader;

  /**
   * Updates the view by first removing all desks and bookings from the view
   * and adding them again. Should be called everytime the view has to be updated
   * or is first shown.
   * @throws IOException
   */
  public void init() throws IOException {
    vbox_desklist.getChildren().clear();
    vbox_activedesks.getChildren().clear();

    List<DeskDto> desks = deskController.desks();
    List<BookingDto> bookings = bookingController.bookings();

    for (DeskDto desk : desks) {
      AnchorPane tisch = create_desklist_desk(desk);
      vbox_desklist.getChildren().add(tisch);
    }

    for (BookingDto booking : bookings) {
      AnchorPane buchung = create_booked_desk(booking);
      vbox_activedesks.getChildren().add(buchung);
    }
  }

  /**
   * Is used to create a graphical representation of a desk in the AdminView
   * when given a DeskDto.<br></br>
   * It consists of a name, roomname, buildingname, an edit button "button_edit"
   * and a delete button "button_delete"
   * @param desk
   * @return  AnchorPane
   */
  private AnchorPane create_desklist_desk(DeskDto desk) {
    AnchorPane anchorPaneDesk = new AnchorPane();
    anchorPaneDesk.setMinHeight(36.0);
    anchorPaneDesk.setPrefHeight(36.0);
    anchorPaneDesk.setMaxHeight(36.0);
    anchorPaneDesk.setMinWidth(300.0);
    anchorPaneDesk.setStyle(
      "-fx-background-color: #bfcbd4; -fx-background-insets: 2;"
    );

    // Adding name to anchorpane
    Text textDeskID = new Text();
    textDeskID.setStrokeWidth(0.0);
    textDeskID.setStrokeType(OUTSIDE);
    textDeskID.setLayoutX(24.0);
    textDeskID.setLayoutY(24.0);
    textDeskID.setText(desk.getName());
    textDeskID.setFill(BLACK);
    anchorPaneDesk.getChildren().add(textDeskID);
    anchorPaneDesk.setLeftAnchor(textDeskID, 24.0);

    // Adding roomname to anchorpane
    Text textDeskRoom = new Text();
    textDeskRoom.setStrokeWidth(0.0);
    textDeskRoom.setStrokeType(OUTSIDE);
    textDeskRoom.setLayoutX(110.0);
    textDeskRoom.setLayoutY(24.0);
    textDeskRoom.setText(desk.getRaum().getRaumName());
    textDeskRoom.setFill(BLACK);
    anchorPaneDesk.getChildren().add(textDeskRoom);
    anchorPaneDesk.setLeftAnchor(textDeskRoom, 110.0);

    // Adding buildingname to anchorpane
    Text textDeskBuilding = new Text();
    textDeskBuilding.setStrokeWidth(0.0);
    textDeskBuilding.setStrokeType(OUTSIDE);
    textDeskBuilding.setLayoutX(200);
    textDeskBuilding.setLayoutY(23.0);
    textDeskBuilding.setText(desk.getGebaeude().getGebaeudeName());
    anchorPaneDesk.getChildren().add(textDeskBuilding);
    anchorPaneDesk.setLeftAnchor(textDeskBuilding, 250.0);

    // Adding editbutton to anchorpane
    Button button_edit = new Button();
    button_edit.setOnAction(
      event -> {
        try {
          button_edit_click(event, desk);
        } catch (IOException e) {
          System.err.println(e.getCause() + e.getMessage());
        }
      }
    );
    button_edit.setLayoutX(184.0);
    button_edit.setLayoutY(5.0);
    button_edit.setText("Bearbeiten");
    button_edit.setMnemonicParsing(false);
    anchorPaneDesk.getChildren().add(button_edit);
    anchorPaneDesk.setRightAnchor(button_edit, 5.0);

    // Adding delete button to anchorpane
    Button button_delete = new Button();
    button_delete.setOnAction(
      event -> {
        try {
          button_delete_desk_click(desk);
        } catch (GenericServiceException | IOException e) {
          System.err.println(e.getCause() + e.getMessage());
        }
      }
    );
    button_delete.setLayoutX(180.0);
    button_delete.setLayoutY(5.0);
    button_delete.setText("Löschen");
    button_delete.setMnemonicParsing(false);
    anchorPaneDesk.getChildren().add(button_delete);
    anchorPaneDesk.setRightAnchor(button_delete, 80.0);

    return anchorPaneDesk;
  }

  /**
   * The method opens the EditView when a button_edit in the view is clicked. <br></br>
   * It sets the springContext, the desk and the {@link AdminViewController}.
   * It also loads the desk details via {@link EditDeskViewController#init()} into the EditView.
   * @param event
   * @param desk
   * @throws IOException
   */
  public void button_edit_click(ActionEvent event, DeskDto desk)
    throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/EditView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    EditDeskViewController editDeskViewController = fxmlLoader.getController();
    editDeskViewController.setSpringContext(springContext);
    editDeskViewController.setDesk(desk);
    editDeskViewController.init();
    editDeskViewController.setAdminViewController(this);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Tisch bearbeiten");
    stage2.show();
  }

  /**
   * This method opens the DeleteDeskView which is a kind of confirmation view
   * whenever a button_delete_desk is clicked. <br></br>
   * It sets the springContext, the desk that is to be deleted and the AdminViewController.
   * @param desk
   * @throws GenericServiceException
   * @throws IOException
   */
  public void button_delete_desk_click(DeskDto desk)
    throws GenericServiceException, IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/DeleteDeskView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    DeleteDeskViewController deleteDeskViewController = fxmlLoader.getController();
    deleteDeskViewController.setSpringContext(springContext);
    deleteDeskViewController.setDesktodelete(desk);
    deleteDeskViewController.setAdminViewController(this);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Tisch löschen");
    stage2.show();
  }

  /**
   * This method is  used to create a graphical representation of a booking in the AdminView
   * when given a BookingDto.<br></br>
   * It consists of a booking date, the deskname, roomname, building name and a button "button_delete_booking".
   * @param booking
   * @return  AnchorPane
   */
  public AnchorPane create_booked_desk(BookingDto booking) {
    AnchorPane anchorPaneGraphicBooking = new AnchorPane();
    anchorPaneGraphicBooking.setMinHeight(36.0);
    anchorPaneGraphicBooking.setPrefHeight(36.0);
    anchorPaneGraphicBooking.setMaxHeight(36.0);
    anchorPaneGraphicBooking.setMinWidth(300.0);
    anchorPaneGraphicBooking.setStyle(
      "-fx-background-color: #bfcbd4; -fx-background-insets: 2;"
    );

    //add booking date to anchorpane
    Text textDateBooking = new Text();
    textDateBooking.setStrokeWidth(0.0);
    textDateBooking.setStrokeType(OUTSIDE);
    textDateBooking.setLayoutX(24.0);
    textDateBooking.setLayoutY(24.0);
    textDateBooking.setText(DateFormat.formatDate(booking.getBookingDate()));
    textDateBooking.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textDateBooking);
    anchorPaneGraphicBooking.setLeftAnchor(textDateBooking, 24.0);

    // Adding deskname to anchorpane
    Text textDeskName = new Text();
    textDeskName.setStrokeWidth(0.0);
    textDeskName.setStrokeType(OUTSIDE);
    textDeskName.setLayoutX(24.0);
    textDeskName.setLayoutY(24.0);
    textDeskName.setText(booking.getDesk().getName());
    textDeskName.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textDeskName);
    anchorPaneGraphicBooking.setLeftAnchor(textDeskName, 110.0);

    // Adding roomname to anchorpane
    Text textRoom = new Text();
    textRoom.setStrokeWidth(0.0);
    textRoom.setStrokeType(OUTSIDE);
    textRoom.setLayoutX(110.0);
    textRoom.setLayoutY(24.0);
    textRoom.setText(booking.getDesk().getRaum().getRaumName());
    textRoom.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textRoom);
    anchorPaneGraphicBooking.setLeftAnchor(textRoom, 180.0);

    // Adding building name to anchorpane
    Text textBuilding = new Text();
    textBuilding.setStrokeWidth(0.0);
    textBuilding.setStrokeType(OUTSIDE);
    textBuilding.setLayoutX(200.0);
    textBuilding.setLayoutY(24.0);
    textBuilding.setText(booking.getDesk().getGebaeude().getGebaeudeName());
    anchorPaneGraphicBooking.getChildren().add(textBuilding);
    anchorPaneGraphicBooking.setLeftAnchor(textBuilding, 250.0);

    // Adding child to parent
    Button button_delete_booking = new Button();
    button_delete_booking.setOnAction(
      event -> {
        try {
          button_delete_booking_click(event, booking);
        } catch (IOException e) {
          System.err.println(e.getCause() + e.getMessage());
        }
      }
    );
    button_delete_booking.setLayoutX(184.0);
    button_delete_booking.setLayoutY(5.0);
    button_delete_booking.setText("Löschen");
    button_delete_booking.setMnemonicParsing(false);
    anchorPaneGraphicBooking.getChildren().add(button_delete_booking);
    anchorPaneGraphicBooking.setRightAnchor(button_delete_booking, 5.0);

    return anchorPaneGraphicBooking;
  }

  /**
   * This method opens the DeleteBookingView which is a kind of confirmation view
   * whenever a button_delete_booking is clicked. <br></br>
   * It sets the springContext, the booking that is to be deleted and the AdminViewController.
   * @param booking
   * @param event
   * @throws GenericServiceException
   * @throws IOException
   */
  public void button_delete_booking_click(
    ActionEvent event,
    BookingDto booking
  )
    throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(
      getClass().getResource("/fxml/AdminDeleteBookingView.fxml")
    );
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    AdminDeleteBookingViewController bookingViewController = fxmlLoader.getController();
    bookingViewController.setSpringContext(springContext);
    bookingViewController.setBookingtodelete(booking);
    bookingViewController.setAdminViewController(this);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Tisch löschen");
    stage2.show();
  }

  /**
   * This method is called when the button_logout is clicked. It sends the user back to the LoginView.
   * @param event
   * @throws IOException
   */
  public void button_logout_click(ActionEvent event) throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setControllerFactory(springContext::getBean);
    fxmlLoader.setLocation(getClass().getResource("/fxml/LoginView.fxml"));
    Parent root = fxmlLoader.load();
    LoginViewController loginViewController = fxmlLoader.getController();
    loginViewController.setSpringContext(springContext);
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.getScene().setRoot(root);
    stage.show();
  }

  /**
   * This method is called when the button_settings is clicked. It sets the SpringContext in the {@link OverallSettingsViewController}
   * and calls {@link OverallSettingsViewController#init()} to .
   * @param event
   * @throws IOException
   */
  public void button_settings_click(ActionEvent event) throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(
      getClass().getResource("/fxml/OverallSettingsView.fxml")
    );
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    OverallSettingsViewController overallSettingsViewController = fxmlLoader.getController();
    overallSettingsViewController.setSpringContext(springContext);
    overallSettingsViewController.init();
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.getScene().setRoot(root);
    stage.show();
  }

  /**
   * This method is called when the button_mainView is clicked. It sets the SpringContext and the user in the {@link MainViewController}
   * and calls {@link MainViewController#init()}.
   * @param event
   * @throws IOException
   */
  public void button_mainView_click(ActionEvent event) throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    MainViewController mainViewController = fxmlLoader.getController();
    mainViewController.setSpringContext(springContext);
    mainViewController.setUser(this.user);
    mainViewController.init();
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.getScene().setRoot(root);
    stage.show();
  }

  public void button_statistics_click(ActionEvent event) {}

  /**
   * This method is called when the add_desk_button is clicked.<br></br>
   * it sets the springContext, the adminViewController and the user in the {@link AddDeskViewController}.
   * @param event
   * @throws IOException
   */
  public void add_desk_button_click(ActionEvent event) throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/AddDeskView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    AddDeskViewController addDeskViewController = fxmlLoader.getController();
    addDeskViewController.setSpringContext(springContext);
    addDeskViewController.setAdminViewController(this);
    addDeskViewController.setUser(this.user);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Neuen Tisch hinzufügen");
    stage2.show();
  }

  /**
   * This method is called when the button_addfeature is clicked.<br></br>
   * It sets the springContext and the AdminViewController in the {@link AddFeatureViewController}.
   * @throws IOException
   */
  public void button_addfeature_click() throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/AddFeatureView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    AddFeatureViewController addFeatureViewController = fxmlLoader.getController();
    addFeatureViewController.setSpringContext(springContext);
    addFeatureViewController.setAdminViewController(this);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Feature hinzufügen");
    stage2.show();
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
