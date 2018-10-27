package sample.controller;

import communication.*;
import javafx.util.StringConverter;
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
    private CheckComboBox<UserInfo> memberComboBox;
    @FXML
    private TextField titleField;

    private ChatClient client;
    private ChatList chatList;
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


    private boolean isInputValid() {
        String errorMessage = "";

        try {
            ChatInfo chat = ChatInfo.New(titleField.getText());
            client.createChat(chat);
            saveChatinDHT(chat);
            List<UserInfo> invites = memberComboBox.getCheckModel().getCheckedItems();
            //client.inviteChatMember(chat, client.getUserInfo(mainApp.getUserName()));
            for(UserInfo user : invites){
                client.inviteChatMember(chat, user);
            }
            System.out.println(chat.getTitle() + "wird der Chatliste hinzugefügt");

        } catch (NoSuchAlgorithmException e) {
            errorMessage += "Etwas ist schief gelaufen";
        } catch (Exception e) {
            e.printStackTrace();
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
        List<ChatInfo> list = chatList.getChats();
        list.add(chat);
        chatList.setChats(list);
        client.saveChatList(chatList);
    }


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        this.client = mainApp.getChatClient();
        this.chatList = mainApp.getChatList();
        this.contactList = mainApp.getContactList();
        memberComboBox.getItems().addAll(contactList.contactsAsObservableList());
        memberComboBox.setConverter(new StringConverter<UserInfo>() {
            @Override
            public String toString(UserInfo user) {
                return user.getUsername();
            }

            @Override
            public UserInfo fromString(String username) {
                try {
                    return contactList.searchUser(username);
                    //return client.getUserInfo(username);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }
}
