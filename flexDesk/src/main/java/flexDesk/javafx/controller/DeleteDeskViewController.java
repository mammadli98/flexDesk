package flexDesk.javafx.controller;

import flexDesk.api.contract.DeskDto;
import flexDesk.api.controller.DeskController;
import flexDesk.backend.services.exceptions.GenericServiceException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DeleteDeskViewController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  @Autowired
  DeskController deskController;

  AdminViewController adminViewController;
  DeskDto desktodelete;

  Stage stage;

  @FXML
  Button button_delete;

  @FXML
  Button button_cancel;

  public void button_delete_click()
    throws GenericServiceException, IOException {
    deskController.delete(desktodelete);
    adminViewController.init();
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void button_cancel_click() {
    Stage stage = (Stage) button_cancel.getScene().getWindow();
    stage.close();
  }

  public void setAdminViewController(AdminViewController adminViewController) {
    this.adminViewController = adminViewController;
  }

  public void setDesktodelete(DeskDto desk) {
    this.desktodelete = desk;
  }

  public DeskDto getDesktodelete() {
    return this.desktodelete;
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {}
}
