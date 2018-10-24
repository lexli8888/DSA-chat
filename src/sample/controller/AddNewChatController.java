package sample.controller;

import communication.ChatClient;
import communication.ChatInfo;
import communication.ChatList;
import org.controlsfx.control.CheckComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by a-003-ebr on 08.10.2018.
 */
public class AddNewChatController {

    @FXML
    private CheckComboBox memberComboBox;
    @FXML
    private TextField titleField;

    private ChatClient client;
    private ChatList chatList;
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


    private boolean isInputValid() {
        String errorMessage = "";

        try {
            ChatInfo chat = ChatInfo.New(titleField.getText());
            System.out.println(chat.getTitle() + "wird der Chatliste hinzugefügt");
            saveChatinDHT(chat);
        } catch (NoSuchAlgorithmException e) {
            errorMessage += "Etwas ist schief gelaufen";
        } catch (Exception e) {
            errorMessage += "Chat konnte nicht erstellt werden";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Ungültige Eingabe");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private void saveChatinDHT(ChatInfo chat) throws Exception {
        List<ChatInfo> list = chatList.getChatsAsList();
        list.add(chat);
        chatList.setChats(list);
        client.saveChatList(chatList);
    }


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        this.client = mainApp.getChatClient();
        this.chatList = mainApp.getChatList();
    }
}
