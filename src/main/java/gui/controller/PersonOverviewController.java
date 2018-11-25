package gui.controller;

import communication.UserInfo;
import gui.util.AlertFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import gui.Main;
import gui.state.DataState;

public class PersonOverviewController implements IDataStateController {
    @FXML
    private TableView<UserInfo> personTable;
    @FXML
    private TableColumn<UserInfo, String> userNameColumn;
    @FXML
    private TableColumn<UserInfo, String> statusColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Button notariatButton;

    private Main mainApp;
    private DataState dataState;

    public PersonOverviewController() {
    }

    private void showPersonDetails(UserInfo user) {
        if (user != null) {
            userNameLabel.setText(user.getUsername());
            firstNameLabel.setText(user.getFirstName());
            lastNameLabel.setText(user.getLastName());
            notariatButton.setDisable(false);
        } else {
            userNameLabel.setText("");
            firstNameLabel.setText("");
            lastNameLabel.setText("");
        }
    }

    @FXML
    private void handleDeletePerson() throws Exception {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            UserInfo user = personTable.getItems().get(selectedIndex);
            dataState.removeContact(user);
        } else {
            // Nothing selected.
            Alert alert = AlertFactory.WarningAlert("No Selection", "Please select a person in the table.");
            alert.initOwner(mainApp.getPrimaryStage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewPerson() throws Exception {
        boolean okClicked = mainApp.addNewContactDialog();
        if (okClicked) {
            Alert alert = AlertFactory.InformationAlert("Success", "Kontakt wurde erfolgreich in Kontaktliste aufgenommen.");
            alert.initOwner(mainApp.getPrimaryStage());
            alert.showAndWait();
        }
    }


    @Override
    public void setState(Main mainApp, DataState state) {
        this.dataState = state;
        this.mainApp = mainApp;

        notariatButton.setDisable(true);

        personTable.setItems(state.getUsers());
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        statusColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(dataState.getOnlineStatus(cellData.getValue().getUsername()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
        showPersonDetails(null);
        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));

    }

    @FXML
    public void showNotariat() throws Exception {
        UserInfo user = dataState.getUser(userNameLabel.getText());
        System.out.println(user.getUsername());
        mainApp.addNotariatFile(user);
    }
}
