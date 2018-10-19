package sample.controller;

import communication.ChatClient;
import communication.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.Main;
import sample.model.Person;
import sample.util.DateUtil;

public class PersonOverviewController {
    @FXML
    private TableView<UserInfo> personTable;
    @FXML
    private TableColumn<UserInfo, String> userNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label userNameLabel;


    private Main mainApp;
    private ChatClient client;

    public PersonOverviewController() {
    }

    private void showPersonDetails(UserInfo user) {
        if (user != null) {
            // Fill the labels with info from the person object.
            userNameLabel.setText(user.getUsername());
            firstNameLabel.setText(user.getFirstName());
            lastNameLabel.setText(user.getLastName());

        } else {
            // Person is null, remove all the text.
            userNameLabel.setText("");
            firstNameLabel.setText("");
            lastNameLabel.setText("");       }
    }

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        //firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        //lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        showPersonDetails(null);

        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        this.client = mainApp.getChatClient();
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.addNewContactDialog();
        if (okClicked) {

        }
    }



}
