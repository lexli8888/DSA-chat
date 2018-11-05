package gui.controller;

import communication.UserInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
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

    private Main mainApp;
    private DataState dataState;

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
    private void handleDeletePerson() throws Exception {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            UserInfo user = personTable.getItems().get(selectedIndex);
            dataState.removeContact(user);
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
    private void handleNewPerson() throws Exception {
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


    @Override
    public void setState(Main mainApp, DataState state) {
        this.dataState = state;
        this.mainApp = mainApp;

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
}
