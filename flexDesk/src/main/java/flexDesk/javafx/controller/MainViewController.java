package flexDesk.javafx.controller;

import flexDesk.api.contract.*;
import flexDesk.api.controller.*;
import flexDesk.backend.entities.derivatedAttributes.Group;
import flexDesk.backend.entities.derivatedAttributes.TimePeriod;
import flexDesk.backend.services.exceptions.GenericServiceException;
import flexDesk.tools.CheckBoxUtil;
import flexDesk.tools.DateFormat;
import flexDesk.tools.GlobalSettings;
import flexDesk.tools.MainViewGraphicElementUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainViewController {

  @Autowired
  private ConfigurableApplicationContext springContext;

  @Autowired
  private DeskController deskController;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private RaumController raumController;

  @Autowired
  MultiDeskBookingController multideskBookingController;

  private UserDto user;

  @Autowired
  private GebaeudeController gebaeudeController;

  @Autowired
  private FeatureController feautureController;

  private List<Long> featureSelection;

  private List<Long> bookedDeskIds;

  private List<Long> morningBookedDeskIds;

  private List<Long> afternoonBookedDeskIds;

  private List<DeskDto> filteredDesks;

  private LocalDate filterDate;
  private TimePeriod mdbPeriod = TimePeriod.FULLDAY;

  private Stage stage;

  private FXMLLoader fxmlLoader;

  private boolean multideskBookingActivated = false;

  @FXML
  Button button_logout;

  @FXML
  Button button_statistics;

  @FXML
  Button button_admin;

  @FXML
  VBox gebaeudeVbox;

  @FXML
  public VBox desksVbox;

  @FXML
  public VBox bookingsVbox;

  @FXML
  VBox raumVbox;

  @FXML
  VBox featureVbox;

  @FXML
  DatePicker datePicker;

  @FXML
  Button buttonMD;

  @FXML
  Button buttonMDBook;

  @FXML
  MenuButton buttonMdbPeriod;

  List<DeskDto> mdbs = new ArrayList<>();

  @FXML
  public void initialize() {}

  public void init() throws IOException {
    this.bookingsVbox.getChildren().clear();
    this.desksVbox.getChildren().clear();
    this.gebaeudeVbox.getChildren().clear();
    this.raumVbox.getChildren().clear();
    this.featureVbox.getChildren().clear();
    this.featureSelection = new ArrayList<>();
    this.filterDate = LocalDate.now();
    this.afternoonBookedDeskIds = new ArrayList<>();
    this.morningBookedDeskIds = new ArrayList<>();
    this.reloadBookedDesks();
    this.addAllDesksToView();
    this.addAllGebaeudeToView();
    this.addAllRoomsToView();
    this.addAllFeaturesToView();
    this.addMyBookingsToView();

    addAllMultideskbookingsToView();
    if (this.getUser().getGroupMember().equals(Group.USER)) {
      this.button_admin.setVisible(false);
      this.button_statistics.setVisible(false);
    } else if (this.getUser().getGroupMember().equals(Group.ACCOUNTANT)) {
      this.button_admin.setVisible(false);
    }
    if (!(this.getUser().isProjectLead())) {
      this.buttonMD.setVisible(false);
    }
    switch (this.mdbPeriod) {
      case FULLDAY:
        buttonMdbPeriod.setText("Ganzer Tag");
        break;
      case AFTERNOON:
        buttonMdbPeriod.setText("Nachmittags");
        break;
      case MORNING:
        buttonMdbPeriod.setText("Vormittags");
        break;
    }
    this.buttonMDBook.setVisible(multideskBookingActivated);
    this.buttonMdbPeriod.setVisible(multideskBookingActivated);

    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
      public DateCell call(final DatePicker datePicker) {
        return new DateCell() {
          @Override
          public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            LocalDate today = LocalDate.now();
            setDisable(
              empty ||
              item.isBefore(today) ||
              item.isAfter(
                today.plusDays(
                  GlobalSettings.getInstance().getBookingTimeFrameLenght()
                )
              )
            );

            if (
              !(
                empty ||
                item.isBefore(today) ||
                item.isAfter(
                  today.plusDays(
                    GlobalSettings.getInstance().getBookingTimeFrameLenght()
                  )
                )
              )
            ) {
              setStyle("-fx-background-color: #4c918b;");
            }
          }
        };
      }
    };
    this.datePicker.setDayCellFactory(dayCellFactory);
    this.datePicker.setPromptText(DateFormat.formatDate(LocalDate.now()));
    this.datePicker.setConverter(DateFormat.getConverter());
  }

  public void addAllMultideskbookingsToView() {
    List<MultiDeskBookingDto> mdbs = multideskBookingController.multiDeskBookings();
    for (MultiDeskBookingDto mdb : mdbs) {
      MainViewGraphicElementUtil.AddElementToView(mdb, this);
    }
  }

  public void buttonLoeschenMDBClick(
    ActionEvent event,
    MultiDeskBookingDto multideskBooking
  )
    throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(
      getClass().getResource("/fxml/DeleteMDBookingView.fxml")
    );
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    DeleteMDBookingViewController deleteMDBookingViewController = fxmlLoader.getController();
    deleteMDBookingViewController.setSpringContext(springContext);
    deleteMDBookingViewController.setMultiDeskBookingDto(multideskBooking);
    deleteMDBookingViewController.setMainViewController(this);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    Scene scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Booking löschen");
    stage2.show();
  }

  public void buttonDetailsMDBClick(
    ActionEvent event,
    MultiDeskBookingDto multideskBooking
  )
    throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(
      getClass().getResource("/fxml/MultideskbookingdetailsView.fxml")
    );
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    MultideskbookingDetailsViewController multideskBookingDetailsViewController = fxmlLoader.getController();
    multideskBookingDetailsViewController.setSpringContext(
      this.getSpringContext()
    );
    multideskBookingDetailsViewController.setMainViewController(this);
    List<DeskDto> mdbDeskList = multideskBooking
      .getBookingList()
      .stream()
      .map(BookingDto::getDesk)
      .collect(Collectors.toList());
    multideskBookingDetailsViewController.setMdbDeskList(mdbDeskList);
    multideskBookingDetailsViewController.init();
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    Scene scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Book Flexdesk");
    stage2.show();
  }

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

  public void button_admin_click(ActionEvent event) throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setControllerFactory(springContext::getBean);
    fxmlLoader.setLocation(getClass().getResource("/fxml/AdminView.fxml"));
    Parent root = fxmlLoader.load();
    AdminViewController adminViewController = fxmlLoader.getController();
    adminViewController.setSpringContext(springContext);
    adminViewController.setUser(this.user);
    adminViewController.init();
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.getScene().setRoot(root);
    stage.show();
  }

  public void button_statistics_click() {
    //unfinished feature
  }

  public void buttonMDClick() throws IOException {
    if (multideskBookingActivated) {
      multideskBookingActivated = false;
      init();
      buttonMD.setText("Multidesk Modus");
    } else {
      multideskBookingActivated = true;
      init();
      buttonMD.setText("Normaler Modus");
    }
  }

  public void mdbFulldayClick() throws IOException {
    this.mdbPeriod = TimePeriod.FULLDAY;
    init();
  }

  public void mdbAfternoonClick() throws IOException {
    this.mdbPeriod = TimePeriod.AFTERNOON;
    init();
  }

  public void mdbMorningClick() throws IOException {
    this.mdbPeriod = TimePeriod.MORNING;
    init();
  }

  public void buttonMDBookClick() throws IOException {
    if (!this.mdbs.isEmpty()) {
      fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(
        getClass().getResource("/fxml/MultideskBookingView.fxml")
      );
      fxmlLoader.setControllerFactory(springContext::getBean);
      Parent root = fxmlLoader.load();
      MultideskBookingViewController multideskBookingViewController = fxmlLoader.getController();
      multideskBookingViewController.setSpringContext(this.getSpringContext());
      multideskBookingViewController.setMainViewController(this);
      multideskBookingViewController.setMdbDeskList(mdbs);
      multideskBookingViewController.init();
      Stage stage2 = new Stage();
      stage2.setResizable(false);
      Scene scene = new Scene(root);
      stage2.setScene(scene);
      stage2.setTitle("Book Flexdesk");
      stage2.show();
    }
  }

  public void checkBoxMultiDeskClick(
    ActionEvent event,
    DeskDto deskToDisplay,
    CheckBox checkBox
  ) {
    if (checkBox.isSelected()) {
      mdbs.add(deskToDisplay);
    } else {
      mdbs.remove(deskToDisplay);
    }
  }

  public void buttonBookClick(ActionEvent event, DeskDto deskToDisplay)
    throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/BookingView.fxml"));
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    BookingViewController bookingViewController = fxmlLoader.getController();
    bookingViewController.setSpringContext(springContext);
    bookingViewController.setMainViewController(this);
    bookingViewController.setUser(this.user);
    bookingViewController.setDesk(deskToDisplay);
    bookingViewController.setText_room(deskToDisplay.getRaum().getRaumName());
    bookingViewController.setText_building(
      deskToDisplay.getGebaeude().getGebaeudeName()
    );
    bookingViewController.setText_InetSpeed(
      deskToDisplay.getInternetGeschwindigkeit()
    );
    bookingViewController.init();
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    Scene scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Book Flexdesk");
    stage2.show();
  }

  public void buttonLoeschenClick(
    ActionEvent event,
    BookingDto bookingToDisplay
  )
    throws GenericServiceException, IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(
      getClass().getResource("/fxml/DeleteBookingView.fxml")
    );
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    DeleteBookingViewController deleteBookingViewController = fxmlLoader.getController();
    deleteBookingViewController.setSpringContext(springContext);
    deleteBookingViewController.setBookingToDelete(bookingToDisplay);
    deleteBookingViewController.setMainViewController(this);
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    Scene scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Booking löschen");
    stage2.show();
  }

  public void buttonDetailsClick(
    ActionEvent event,
    BookingDto bookingToDisplay
  )
    throws IOException {
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(
      getClass().getResource("/fxml/BookingDetailsView.fxml")
    );
    fxmlLoader.setControllerFactory(springContext::getBean);
    Parent root = fxmlLoader.load();
    BookingDetailsController bookingDetailsController = fxmlLoader.getController();
    bookingDetailsController.setSpringContext(springContext);
    bookingDetailsController.setDesk(bookingToDisplay.getDesk());
    bookingDetailsController.init();
    bookingDetailsController.setText_room(
      bookingToDisplay.getDesk().getRaum().getRaumName()
    );
    bookingDetailsController.setText_building(
      bookingToDisplay.getDesk().getGebaeude().getGebaeudeName()
    );
    bookingDetailsController.setText_InetSpeed(
      bookingToDisplay.getDesk().getInternetGeschwindigkeit()
    );
    bookingDetailsController.setTextDate(
      bookingToDisplay.getBookingDate().toString()
    );
    bookingDetailsController.setTextTimePeriod(
      bookingToDisplay.getBookingPeriod().toString().toLowerCase(Locale.ROOT)
    );
    Stage stage2 = new Stage();
    stage2.setResizable(false);
    Scene scene = new Scene(root);
    stage2.setScene(scene);
    stage2.setTitle("Book Flexdesk");
    stage2.show();
  }

  private boolean featureSelectionIsSubset(DeskDto deskDto) {
    List<Long> deskFeatureIds = deskDto
      .getFeatures()
      .stream()
      .map(FeatureDto::getFeatureId)
      .collect(Collectors.toList());

    return featureSelection
      .stream()
      .map(deskFeatureIds::contains)
      .reduce(true, (x, y) -> x && y);
  }

  private boolean bookedOnDate(BookingDto bookingDto) {
    if (GlobalSettings.getInstance().isParentalModeSetting()) {
      if (bookingDto.getBookingDate().isEqual(filterDate)) {
        if (bookingDto.getBookingPeriod().equals(TimePeriod.MORNING)) {
          morningBookedDeskIds.add(bookingDto.getDesk().getDeskId());
        } else if (bookingDto.getBookingPeriod().equals(TimePeriod.AFTERNOON)) {
          afternoonBookedDeskIds.add(bookingDto.getDesk().getDeskId());
        }
        return true;
      }
      if (
        bookingDto.getBookingDate().isEqual(filterDate) &&
        (
          bookingDto.getBookingPeriod().equals(mdbPeriod) ||
          bookingDto.getBookingPeriod().equals(TimePeriod.FULLDAY)
        )
      ) {
        return true;
      }
    }
    return bookingDto.getBookingDate().isEqual(filterDate);
  }

  private boolean isDeskBooked(Long deskId) {
    boolean booked;
    booked = bookedDeskIds.contains(deskId);
    if (GlobalSettings.getInstance().isParentalModeSetting()) {
      booked =
        (morningBookedDeskIds.contains(deskId)) &&
        afternoonBookedDeskIds.contains((deskId));
    }
    return booked;
  }

  private boolean isDeskFreeInPeriod(Long deskId) {
    boolean inTimePeriod = true;
    if (
      GlobalSettings.getInstance().isParentalModeSetting() &&
      multideskBookingActivated
    ) {
      switch (mdbPeriod) {
        case FULLDAY:
          inTimePeriod =
            !(
              (morningBookedDeskIds.contains(deskId)) ||
              afternoonBookedDeskIds.contains((deskId))
            );
          break;
        case AFTERNOON:
          inTimePeriod = !(afternoonBookedDeskIds.contains(deskId));
          break;
        case MORNING:
          inTimePeriod = !(morningBookedDeskIds.contains(deskId));
          break;
      }
    }
    return inTimePeriod;
  }

  public void reloadDesks() {
    desksVbox.getChildren().clear();

    filteredDesks
      .stream()
      .filter(deskDto -> !isDeskBooked(deskDto.getDeskId()))
      .filter(deskDto -> isDeskFreeInPeriod(deskDto.getDeskId()))
      .filter(this::featureSelectionIsSubset)
      .forEach(
        deskDto -> MainViewGraphicElementUtil.AddElementToView(deskDto, this)
      );
  }

  public void reloadBookedDesks() {
    bookingController
      .bookings()
      .stream()
      .filter(bookingDto -> !bookingDto.isDeletedFlag())
      .filter(
        bookingDto -> bookingDto.getBookingDate().isBefore(LocalDate.now())
      )
      .forEach(
        bookingDto -> {
          try {
            bookingController.delete(bookingDto);
          } catch (GenericServiceException e) {
            System.err.println(e.getCause() + e.getMessage());
          }
        }
      );

    morningBookedDeskIds.clear();
    afternoonBookedDeskIds.clear();

    bookedDeskIds =
      bookingController
        .bookings()
        .stream()
        .filter(this::bookedOnDate)
        .map(BookingDto::getDesk)
        .map(DeskDto::getDeskId)
        .collect(Collectors.toList());
  }

  private void addMyBookingsToView() {
    List<BookingDto> bookingDtos = bookingController
      .bookings()
      .stream()
      .filter(bookingDto -> !bookingDto.isMdb())
      .collect(Collectors.toList());

    int size = bookingDtos.size();
    int removedCount = 0;
    for (int i = 0; i < size; i++) {
      BookingDto bookingDto = bookingDtos.get(i - removedCount);
      if (
        !(bookingDto.getUser().getUserId().equals(this.getUser().getUserId()))
      ) {
        bookingDtos.remove(bookingDto);
        removedCount++;
      }
    }
    bookingDtos
      .stream()
      .forEach(
        bookingDto -> {
          try {
            MainViewGraphicElementUtil.AddElementToView(bookingDto, this);
          } catch (Exception e) {
            System.err.println(e.getCause() + e.getMessage());
          }
        }
      );
  }

  private void addAllGebaeudeToView() {
    List<GebaeudeDto> gebaeudeDtos = gebaeudeController.gebaeude();

    gebaeudeDtos
      .stream()
      .forEach(
        gebaeudeDto ->
          CheckBoxUtil.addSelectableToVbox(
            gebaeudeDto,
            this.gebaeudeVbox,
            event -> {
              try {
                changeByGebaudeCheckBox();
              } catch (Exception e) {
                System.err.println(e.getCause() + e.getMessage());
              }
            }
          )
      );
  }

  private void addAllDesksToView() {
    filteredDesks = deskController.desks();
    reloadDesks();
  }

  private void addAllRoomsToView() {
    List<RaumDto> raumDtos = raumController.rooms();
    raumDtos
      .stream()
      .forEach(
        raumDto ->
          CheckBoxUtil.addSelectableToVbox(
            raumDto,
            this.raumVbox,
            event -> {
              try {
                changeByRaumCheckBox();
              } catch (Exception e) {
                System.err.println(e.getCause() + e.getMessage());
              }
            }
          )
      );
  }

  private void addAllFeaturesToView() {
    List<FeatureDto> featureDtos = feautureController.features();
    featureDtos
      .stream()
      .forEach(
        featureDto ->
          CheckBoxUtil.addSelectableToVbox(
            featureDto,
            this.featureVbox,
            event -> {
              try {
                changeByFeatureCheckBox();
              } catch (Exception e) {
                System.err.println(e.getCause() + e.getMessage());
              }
            }
          )
      );
  }

  public void changeByGebaudeCheckBox() {
    desksVbox.getChildren().clear();
    raumVbox.getChildren().clear();
    filteredDesks.clear();

    List<CheckBox> selection = gebaeudeVbox
      .getChildren()
      .stream()
      .map(CheckBoxUtil::nodeToCheckBox)
      .collect(Collectors.toList());

    if (
      selection
        .stream()
        .map(CheckBox::isSelected)
        .reduce(false, (x, y) -> x || y)
    ) {
      selection
        .stream()
        .filter(CheckBox::isSelected)
        .map(Node::getId)
        .forEach(
          gebaeudeId -> {
            try {
              reloadByGebaeudeSelection(
                gebaeudeController.gebaeudeIdToGebaeudeDto(
                  Long.parseLong(gebaeudeId)
                )
              );
            } catch (GenericServiceException | IOException e) {
              System.err.println(e.getCause() + e.getMessage());
            }
          }
        );
      reloadDesks();
    } else {
      addAllDesksToView();
      addAllRoomsToView();
    }
  }

  public void changeByRaumCheckBox() {
    desksVbox.getChildren().clear();
    filteredDesks.clear();
    raumVbox
      .getChildren()
      .stream()
      .map(CheckBoxUtil::nodeToCheckBox)
      .filter(CheckBox::isSelected)
      .map(Node::getId)
      .forEach(
        id -> {
          try {
            reloadByRaumSelection(
              raumController.raumIdToRaumDto(Long.parseLong(id))
            );
          } catch (GenericServiceException | IOException e) {
            System.err.println(e.getCause() + e.getMessage());
          }
        }
      );

    if (
      !raumVbox
        .getChildren()
        .stream()
        .map(node -> CheckBoxUtil.nodeToCheckBox(node).isSelected())
        .reduce(false, (x, y) -> x || y)
    ) {
      changeByGebaudeCheckBox();
    }
    reloadDesks();
  }

  public void changeByFeatureCheckBox() {
    this.featureSelection =
      featureVbox
        .getChildren()
        .stream()
        .map(CheckBoxUtil::nodeToCheckBox)
        .filter(CheckBox::isSelected)
        .map(Node::getId)
        .map(Long::parseLong)
        .collect(Collectors.toList());

    reloadDesks();
  }

  public void changeByDatePicker() {
    this.filterDate = datePicker.getValue();
    reloadBookedDesks();
    reloadDesks();
  }

  public void reloadByGebaeudeSelection(GebaeudeDto gebaeudeDto)
    throws GenericServiceException, IOException {
    List<DeskDto> deskDtos = deskController.filterByGebaeude(gebaeudeDto);
    filteredDesks.addAll(deskDtos);

    List<RaumDto> raumDtos = raumController.filterByGebaude(gebaeudeDto);
    raumDtos
      .stream()
      .forEach(
        raumDto ->
          CheckBoxUtil.addSelectableToVbox(
            raumDto,
            this.raumVbox,
            event -> {
              try {
                changeByRaumCheckBox();
              } catch (Exception e) {
                throw new RuntimeException();
              }
            }
          )
      );
  }

  private void reloadByRaumSelection(RaumDto raumDto)
    throws GenericServiceException, IOException {
    List<DeskDto> deskDtos = deskController.filterByRaum(raumDto);
    filteredDesks.addAll(deskDtos);
  }

  public ConfigurableApplicationContext getSpringContext() {
    return springContext;
  }

  public void setSpringContext(ConfigurableApplicationContext springContext) {
    this.springContext = springContext;
  }

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }

  public LocalDate getFilterDate() {
    return filterDate;
  }

  public TimePeriod getMdbPeriod() {
    return mdbPeriod;
  }

  public boolean isMultideskBookingActivated() {
    return multideskBookingActivated;
  }

  public void setMultideskBookingActivated(boolean multideskBookingActivated) {
    this.multideskBookingActivated = multideskBookingActivated;
  }

  public List<Long> getMorningBookedDeskIds() {
    return morningBookedDeskIds;
  }

  public List<Long> getAfternoonBookedDeskIds() {
    return afternoonBookedDeskIds;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
