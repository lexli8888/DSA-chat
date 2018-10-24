package sample.controller;

import communication.ChatClient;
import communication.ContactList;
import communication.UserInfo;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.Main;
import sample.model.Person;
import sample.util.DateUtil;

import java.util.List;

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
    private ContactList contactList;


    public PersonOverviewController() {
    }

    private void showPersonDetails(UserInfo user) {
        if (user != null) {
            userNameLabel.setText(user.getUsername());
            firstNameLabel.setText(user.getFirstName());
            lastNameLabel.setText(user.getLastName());

        } else {
            userNameLabel.setText("");
            firstNameLabel.setText("");
            lastNameLabel.setText("");       }
    }

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        userNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        showPersonDetails(null);
        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    public void setMainApp(Main mainApp)  {
        this.mainApp = mainApp;
        this.client = mainApp.getChatClient();
        this.contactList = mainApp.getContactList();
        personTable.setItems(mainApp.getContactList().getContactsAsObservableList());
    }

    @FXML
    private void handleDeletePerson() throws Exception {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            List<UserInfo> contacts = contactList.getContactsAsList();
            UserInfo userToRemove = personTable.getItems().get(selectedIndex);
            contacts.remove(userToRemove);
            contactList.setContacts(contacts);
            client.saveContactList(contactList);
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
        boolean okClicked = mainApp.addNewContactDialog();
        if (okClicked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Success");
            alert.setHeaderText("Kontakt hinzugefügt");
            alert.setContentText("Kontakt wurde erfolgreich in Kontaktliste aufgenommen.");

            alert.showAndWait();
        }
    }



}
