package gui.controller;

import communication.UserInfo;
import gui.util.AlertFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import gui.Main;
import gui.state.DataState;

public class AddNewContactController implements IDataStateModalController {

    @FXML
    private TextField userName;

    private Stage dialogStage;
    private DataState dataState;

    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        try {
            UserInfo contact = dataState.getUser(userName.getText());
            if (contact == null) {
                throw new Exception("user not found");
            }
            System.out.println(userName.toString() + "wird der Kontaktliste hinzugefügt");
            dataState.addContact(contact);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "User " + userName.getText() + " wurde nicht gefunden! Username überprüfen.";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = AlertFactory.ErrorAlert("Invalid Fields", errorMessage);
            alert.initOwner(dialogStage);
            alert.showAndWait();

            return false;
        }
    }

    @Override
    public void setState(Main mainApp, DataState state) {
        this.dataState = state;
    }
}
