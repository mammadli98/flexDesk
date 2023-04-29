package flexDesk.tools;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.shape.StrokeType.OUTSIDE;

import flexDesk.api.contract.BookingDto;
import flexDesk.api.contract.DeskDto;
import flexDesk.api.contract.MultiDeskBookingDto;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.services.exceptions.GenericServiceException;
import flexDesk.javafx.controller.MainViewController;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public final class MainViewGraphicElementUtil {

  public static void AddElementToView(
    MultiDeskBookingDto multideskBooking,
    MainViewController mainViewController
  ) {
    mainViewController.reloadBookedDesks();
    mainViewController.reloadDesks();
    AnchorPane anchorPaneGraphicBooking = new AnchorPane();
    anchorPaneGraphicBooking.setMinHeight(36.0);
    anchorPaneGraphicBooking.setPrefHeight(36.0);
    anchorPaneGraphicBooking.setMaxHeight(36.0);
    anchorPaneGraphicBooking.setMinWidth(300.0);
    anchorPaneGraphicBooking.setStyle(
      "-fx-background-color: #dc9cff; -fx-background-insets: 2;"
    );

    // Adding child to parent
    Text textDateBooking = new Text();
    textDateBooking.setStrokeWidth(0.0);
    textDateBooking.setStrokeType(OUTSIDE);
    textDateBooking.setLayoutX(24.0);
    textDateBooking.setLayoutY(24.0);
    textDateBooking.setText(DateFormat.formatDate(multideskBooking.getDate()));
    textDateBooking.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textDateBooking);
    anchorPaneGraphicBooking.setLeftAnchor(textDateBooking, 24.0);

    // Adding child to parent
    Text textTimePeriodBooking = new Text();
    textTimePeriodBooking.setStrokeWidth(0.0);
    textTimePeriodBooking.setStrokeType(OUTSIDE);
    textTimePeriodBooking.setLayoutX(171.0);
    textTimePeriodBooking.setLayoutY(23.0);
    if (multideskBooking.getTimePeriod().equals(TimePeriod.FULLDAY)) {
      textTimePeriodBooking.setText("Ganzer Tag");
    } else if (multideskBooking.getTimePeriod().equals(TimePeriod.MORNING)) {
      textTimePeriodBooking.setText("Vormittags");
    } else if (multideskBooking.getTimePeriod().equals(TimePeriod.AFTERNOON)) {
      textTimePeriodBooking.setText("Nachmittags");
    }

    anchorPaneGraphicBooking.getChildren().add(textTimePeriodBooking);
    anchorPaneGraphicBooking.setLeftAnchor(textTimePeriodBooking, 180.0);

    // Adding child to parent
    Button buttonDetailsBooking = new Button();
    buttonDetailsBooking.setOnAction(
      event -> {
        try {
          mainViewController.buttonDetailsMDBClick(event, multideskBooking);
        } catch (IOException e) {
          System.err.println(e.getCause() + e.getMessage());
        }
      }
    );
    buttonDetailsBooking.setLayoutX(184.0);
    buttonDetailsBooking.setLayoutY(5.0);
    buttonDetailsBooking.setText("Details");
    buttonDetailsBooking.setMnemonicParsing(false);
    anchorPaneGraphicBooking.getChildren().add(buttonDetailsBooking);
    anchorPaneGraphicBooking.setRightAnchor(buttonDetailsBooking, 5.0);

    // Adding child to parent
    Button buttonLoeschenBooking = new Button();
    buttonLoeschenBooking.setOnAction(
      event -> {
        try {
          mainViewController.buttonLoeschenMDBClick(event, multideskBooking);
        } catch (IOException e) {
          System.err.println(e.getCause() + e.getMessage());
        }
      }
    );
    buttonLoeschenBooking.setLayoutX(180.0);
    buttonLoeschenBooking.setLayoutY(5.0);
    buttonLoeschenBooking.setText("Löschen");
    buttonLoeschenBooking.setMnemonicParsing(false);
    anchorPaneGraphicBooking.getChildren().add(buttonLoeschenBooking);
    anchorPaneGraphicBooking.setRightAnchor(buttonLoeschenBooking, 60.0);

    // Adding graphicDesk to view
    mainViewController.bookingsVbox.getChildren().add(anchorPaneGraphicBooking);
  }

  public static void AddElementToView(
    DeskDto deskToDisplay,
    MainViewController mainViewController
  ) {
    AnchorPane anchorPaneGraphicBooking = new AnchorPane();
    anchorPaneGraphicBooking.setMinHeight(36.0);
    anchorPaneGraphicBooking.setPrefHeight(36.0);
    anchorPaneGraphicBooking.setMaxHeight(36.0);
    anchorPaneGraphicBooking.setMinWidth(300.0);

    if (GlobalSettings.getInstance().isParentalModeSetting()) {
      if (
        mainViewController
          .getMorningBookedDeskIds()
          .contains(deskToDisplay.getDeskId())
      ) {
        anchorPaneGraphicBooking.setStyle(
          "-fx-background-color: #aca6ff; -fx-background-insets: 2;"
        );
      } else if (
        mainViewController
          .getAfternoonBookedDeskIds()
          .contains(deskToDisplay.getDeskId())
      ) {
        anchorPaneGraphicBooking.setStyle(
          "-fx-background-color: #ffcc85; -fx-background-insets: 2;"
        );
      } else {
        anchorPaneGraphicBooking.setStyle(
          "-fx-background-color: #bfcbd4; -fx-background-insets: 2;"
        );
      }
    } else {
      anchorPaneGraphicBooking.setStyle(
        "-fx-background-color: #bfcbd4; -fx-background-insets: 2;"
      );
    }

    // Adding child to parent
    Text textDeskName = new Text();
    textDeskName.setStrokeWidth(0.0);
    textDeskName.setStrokeType(OUTSIDE);
    textDeskName.setLayoutX(24.0);
    textDeskName.setLayoutY(24.0);
    textDeskName.setText(deskToDisplay.getName());
    textDeskName.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textDeskName);
    anchorPaneGraphicBooking.setLeftAnchor(textDeskName, 24.0);

    // Adding child to parent
    Text textRoom = new Text();
    textRoom.setStrokeWidth(0.0);
    textRoom.setStrokeType(OUTSIDE);
    textRoom.setLayoutX(110.0);
    textRoom.setLayoutY(24.0);
    textRoom.setText(deskToDisplay.getRaum().getRaumName());
    textRoom.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textRoom);
    anchorPaneGraphicBooking.setLeftAnchor(textRoom, 110.0);

    // Adding child to parent
    Text textBuilding = new Text();
    textBuilding.setStrokeWidth(0.0);
    textBuilding.setStrokeType(OUTSIDE);
    textBuilding.setLayoutX(200.0);
    textBuilding.setLayoutY(24.0);
    textBuilding.setText(deskToDisplay.getGebaeude().getGebaeudeName());
    anchorPaneGraphicBooking.getChildren().add(textBuilding);
    anchorPaneGraphicBooking.setLeftAnchor(textBuilding, 250.0);

    if (GlobalSettings.getInstance().isParentalModeSetting()) {
      Text textBuchbar = new Text();
      textBuchbar.setStrokeWidth(0.0);
      textBuchbar.setStrokeType(OUTSIDE);
      textBuchbar.setLayoutX(200.0);
      textBuchbar.setLayoutY(24.0);
      if (
        mainViewController
          .getMorningBookedDeskIds()
          .contains(deskToDisplay.getDeskId())
      ) {
        textBuchbar.setText("Nachmittags buchbar");
      } else if (
        mainViewController
          .getAfternoonBookedDeskIds()
          .contains(deskToDisplay.getDeskId())
      ) {
        textBuchbar.setText("Vormittags buchbar");
      } else {
        textBuchbar.setText("Ganztägig buchbar");
      }
      anchorPaneGraphicBooking.getChildren().add(textBuchbar);
      anchorPaneGraphicBooking.setLeftAnchor(textBuchbar, 360.0);
    }

    if (mainViewController.isMultideskBookingActivated()) {
      // Multidesk checkbox
      CheckBox checkBoxMultiDesk = new CheckBox();
      checkBoxMultiDesk.setOnAction(
        event ->
          mainViewController.checkBoxMultiDeskClick(
            event,
            deskToDisplay,
            checkBoxMultiDesk
          )
      );
      checkBoxMultiDesk.setLayoutY(5.0);
      checkBoxMultiDesk.setMnemonicParsing(false);
      anchorPaneGraphicBooking.getChildren().add(checkBoxMultiDesk);
      anchorPaneGraphicBooking.setRightAnchor(checkBoxMultiDesk, 5.0);
    } else {
      // Adding Button
      Button buttonBook = new Button();
      buttonBook.setOnAction(
        event -> {
          try {
            mainViewController.buttonBookClick(event, deskToDisplay);
          } catch (IOException e) {
            System.err.println(e.getCause() + e.getMessage());
          }
        }
      );
      buttonBook.setLayoutX(184.0);
      buttonBook.setLayoutY(5.0);
      buttonBook.setText("Buchen");
      buttonBook.setMnemonicParsing(false);
      anchorPaneGraphicBooking.getChildren().add(buttonBook);
      anchorPaneGraphicBooking.setRightAnchor(buttonBook, 5.0);
    }

    // Adding graphicDesk to view
    mainViewController.desksVbox.getChildren().add(anchorPaneGraphicBooking);
  }

  public static void AddElementToView(
    BookingDto bookingToDisplay,
    MainViewController mainViewController
  ) {
    mainViewController.reloadBookedDesks();
    mainViewController.reloadDesks();
    AnchorPane anchorPaneGraphicBooking = new AnchorPane();
    anchorPaneGraphicBooking.setMinHeight(36.0);
    anchorPaneGraphicBooking.setPrefHeight(36.0);
    anchorPaneGraphicBooking.setMaxHeight(36.0);
    anchorPaneGraphicBooking.setMinWidth(300.0);
    anchorPaneGraphicBooking.setStyle(
      "-fx-background-color: #bfcbd4; -fx-background-insets: 2;"
    );

    // Adding child to parent
    Text textDateBooking = new Text();
    textDateBooking.setStrokeWidth(0.0);
    textDateBooking.setStrokeType(OUTSIDE);
    textDateBooking.setLayoutX(24.0);
    textDateBooking.setLayoutY(24.0);
    textDateBooking.setText(
      DateFormat.formatDate(bookingToDisplay.getBookingDate())
    );
    textDateBooking.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textDateBooking);
    anchorPaneGraphicBooking.setLeftAnchor(textDateBooking, 24.0);

    // Adding child to parent
    Text textDeskNrBooking = new Text();
    textDeskNrBooking.setStrokeWidth(0.0);
    textDeskNrBooking.setStrokeType(OUTSIDE);
    textDeskNrBooking.setLayoutX(110.0);
    textDeskNrBooking.setLayoutY(24.0);
    textDeskNrBooking.setText(bookingToDisplay.getDesk().getName());
    textDeskNrBooking.setFill(BLACK);
    anchorPaneGraphicBooking.getChildren().add(textDeskNrBooking);
    anchorPaneGraphicBooking.setLeftAnchor(textDeskNrBooking, 110.0);

    // Adding child to parent
    Text textTimePeriodBooking = new Text();
    textTimePeriodBooking.setStrokeWidth(0.0);
    textTimePeriodBooking.setStrokeType(OUTSIDE);
    textTimePeriodBooking.setLayoutX(171.0);
    textTimePeriodBooking.setLayoutY(23.0);
    if (bookingToDisplay.getBookingPeriod().equals(TimePeriod.FULLDAY)) {
      textTimePeriodBooking.setText("Ganzer Tag");
    } else if (bookingToDisplay.getBookingPeriod().equals(TimePeriod.MORNING)) {
      textTimePeriodBooking.setText("Vormittags");
    } else if (
      bookingToDisplay.getBookingPeriod().equals(TimePeriod.AFTERNOON)
    ) {
      textTimePeriodBooking.setText("Nachmittags");
    }

    anchorPaneGraphicBooking.getChildren().add(textTimePeriodBooking);
    anchorPaneGraphicBooking.setLeftAnchor(textTimePeriodBooking, 180.0);

    // Adding child to parent
    Button buttonDetailsBooking = new Button();
    buttonDetailsBooking.setOnAction(
      event -> {
        try {
          mainViewController.buttonDetailsClick(event, bookingToDisplay);
        } catch (IOException e) {
          System.err.println(e.getCause() + e.getMessage());
        }
      }
    );
    buttonDetailsBooking.setLayoutX(184.0);
    buttonDetailsBooking.setLayoutY(5.0);
    buttonDetailsBooking.setText("Details");
    buttonDetailsBooking.setMnemonicParsing(false);
    anchorPaneGraphicBooking.getChildren().add(buttonDetailsBooking);
    anchorPaneGraphicBooking.setRightAnchor(buttonDetailsBooking, 5.0);

    // Adding child to parent
    Button buttonLoeschenBooking = new Button();
    buttonLoeschenBooking.setOnAction(
      event -> {
        try {
          mainViewController.buttonLoeschenClick(event, bookingToDisplay);
        } catch (GenericServiceException | IOException e) {
          System.err.println(e.getCause() + e.getMessage());
        }
      }
    );
    buttonLoeschenBooking.setLayoutX(180.0);
    buttonLoeschenBooking.setLayoutY(5.0);
    buttonLoeschenBooking.setText("Löschen");
    buttonLoeschenBooking.setMnemonicParsing(false);
    anchorPaneGraphicBooking.getChildren().add(buttonLoeschenBooking);
    anchorPaneGraphicBooking.setRightAnchor(buttonLoeschenBooking, 60.0);

    // Adding graphicDesk to view
    mainViewController.bookingsVbox.getChildren().add(anchorPaneGraphicBooking);
  }
}
