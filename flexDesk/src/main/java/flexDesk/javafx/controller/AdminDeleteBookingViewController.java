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
public class AdminDeleteBookingViewController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  @Autowired
  BookingController bookingController;

  AdminViewController adminViewController;
  BookingDto bookingtodelete;

  Stage stage;

  @FXML
  Button button_delete;

  @FXML
  Button button_cancel;

  public void button_delete_click()
    throws GenericServiceException, IOException {
    bookingController.delete(bookingtodelete);
    adminViewController.init();
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void button_cancel_click() {
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {
    this.springContext = springContext;
  }

  public void setAdminViewController(AdminViewController adminViewController) {
    this.adminViewController = adminViewController;
  }

  public void setBookingtodelete(BookingDto booking) {
    this.bookingtodelete = booking;
  }
}
