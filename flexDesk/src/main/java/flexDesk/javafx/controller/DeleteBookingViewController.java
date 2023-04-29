package flexDesk.javafx.controller;

import flexDesk.api.contract.BookingDto;
import flexDesk.api.controller.BookingController;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DeleteBookingViewController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  @Autowired
  BookingController bookingController;

  BookingDto bookingToDelete;

  MainViewController mainViewController;

  Stage stage;

  @FXML
  Button button_delete;

  @FXML
  Button button_cancel;

  public void button_delete_click()
    throws GenericServiceException, IOException {
    bookingController.delete(bookingToDelete);
    mainViewController.init();
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void button_cancel_click() {
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void setBookingToDelete(BookingDto bookingToDelete) {
    this.bookingToDelete = bookingToDelete;
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {}

  public void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }
}
