package sample.controller;

import communication.ChatClient;
import communication.ChatList;
import communication.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.model.Person;
import sample.util.DateUtil;

/**
 * Created by a-003-ebr on 01.10.2018.
 */
public class AddNewContactController {

    @FXML
    private TextField userName;
    private ChatClient client;
    private Stage dialogStage;
    private Main mainApp;
    private boolean okClicked = false;

    @FXML
    private void initialize() {

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }



    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() throws Exception {
        if (isInputValid()) {


            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() throws Exception {
        String errorMessage = "";

        UserInfo contact = client.getUserInfo(userName.getText());



        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    public void setClient(ChatClient client) {
        this.client = client;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

    }
}
