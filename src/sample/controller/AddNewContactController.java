package sample.controller;

import communication.ChatClient;
import communication.ContactList;
import communication.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;

import java.util.List;

/**
 * Created by a-003-ebr on 01.10.2018.
 */
public class AddNewContactController {

    @FXML
    private TextField userName;
    private ChatClient client;
    private ContactList contactList;
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

    private boolean isInputValid()  {
        String errorMessage = "";
        try {
            UserInfo contact = client.getUserInfo(mainApp.getUserName());
            System.out.println(contact.getUsername() + "wird der Kontaktliste hinzugefügt");
            saveContactinDHT(contact);


        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "User " + userName.getText() + " wurde nicht gefunden! Username überprüfen.";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Ungültiger Username");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private void saveContactinDHT(UserInfo contact) throws Exception {
        List<UserInfo> list = contactList.getContactsAsList();
        list.add(contact);
        contactList.setContacts(list);
        client.saveContactList(contactList);
    }


    public void setMainApp(Main mainApp) throws Exception {
        this.mainApp = mainApp;
        this.client = mainApp.getChatClient();
        this.contactList = client.getContactList();
    }
}
