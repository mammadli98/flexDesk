package flexDesk;

import flexDesk.javafx.controller.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App extends Application {

  private ConfigurableApplicationContext springContext;
  private FXMLLoader fxmlLoader;

  public static void main(String[] args) {
    launch(args);
  }

  @Override //@PostConstruct
  public void init() throws Exception {
    springContext = SpringApplication.run(App.class);
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setControllerFactory(springContext::getBean);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    fxmlLoader.setLocation(getClass().getResource("/fxml/LoginView.fxml"));
    Parent rootNode = fxmlLoader.load();

    LoginViewController loginViewController = fxmlLoader.getController();
    loginViewController.setSpringContext(springContext);

    primaryStage.setMaximized(true);
    primaryStage.setTitle("Flex Desk Application");
    primaryStage.setScene(new Scene(rootNode, 600, 400));
    primaryStage.show();
  }

  @Override
  public void stop() {
    springContext.close();
  }
}
