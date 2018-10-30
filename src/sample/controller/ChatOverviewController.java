package sample.controller;

import communication.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import sample.Main;

import java.util.List;

/**
 * Created by a-003-ebr on 01.10.2018.
 */
public class ChatOverviewController {

    @FXML
    private TableView<ChatInfo> chatsTable;
    @FXML
    private TableColumn<ChatInfo, String> chatsColumn;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextArea inputTextArea;

    private Main mainApp;
    private ChatClient client;
    private ChatList chatList;

    public ChatOverviewController() {
    }


    private void showChatMessages(ChatInfo chat) throws Exception {
        if (chat != null) {
            chatTextArea.clear();
            List<ChatMessage> chatMessages = client.getMessages(chat);
            if(chatMessages != null){
                for(int i =0; i < chatMessages.size(); i++){
                    chatTextArea.appendText(chatMessages.get(i).getSender().getUsername() + "> " + chatMessages.get(i).getText());

                    chatTextArea.appendText("\n");
                }
            }
        } else {
            // chat is null, remove all the text.
            chatTextArea.setText("");
        }

    }

    @FXML
    private void initialize() throws Exception {
        // Initialize the chat table with the two columns
        chatsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        showChatMessages(null);
        chatsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                showChatMessages(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setMainApp(Main mainApp) throws Exception{
        this.mainApp = mainApp;
        this.client = mainApp.getChatClient();
        this.chatList = client.getChatList();

        ObservableList<ChatInfo> observableList = FXCollections.observableArrayList();
        observableList.addAll(chatList.getChats());

        chatsTable.setItems(observableList);
    }

    @FXML
    private void handleDeleteChat() throws Exception {
        int selectedIndex = chatsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            List<ChatInfo> chats = chatList.getChats();
            ChatInfo chatToRemove = chatsTable.getItems().get(selectedIndex);
            chats.remove(chatToRemove);
            chatList.setChats(chats);
            client.saveChatList(chatList);
            chatsTable.getItems().remove(selectedIndex);
        } else {
            nothingSelected();
        }
    }

    @FXML
    private void handleNewChat() {
        boolean okClicked = mainApp.addNewChatDialog();
        if (okClicked) {

            ObservableList<ChatInfo> observableList = FXCollections.observableArrayList();
            observableList.addAll(chatList.getChats());

            chatsTable.setItems(observableList);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Success");
            alert.setHeaderText("Chat erstellt");
            alert.setContentText("Ein neuer Chat wurde erfolgreich erstellt");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewChatEntry() throws Exception {
        int selectedIndex = chatsTable.getSelectionModel().getSelectedIndex();
        if (inputTextArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Text");
            alert.setHeaderText("No text to send");
            alert.setContentText("Please write some text.");

            alert.showAndWait();

        } else if (selectedIndex < 0) {
           nothingSelected();

        } else {
                ChatInfo chat = chatsTable.getSelectionModel().getSelectedItem();
                // Schreibt (auf localhost) zurzeit als Absender noch ein null object, darum können keine Chats verschickt werden.
                ChatMessage message = ChatMessage.New(client.getUserInfo(mainApp.getUserName()), inputTextArea.getText());
                inputTextArea.setText("");
                if(client.sendMessage(chat, message)){
                    showChatMessages(chat);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.setTitle("Message Warning");
                    alert.setHeaderText("Message not sent");
                    alert.setContentText("Houston, we have a problem. Check your Network settings and retry to send the message.");

                    alert.showAndWait();
                }
            }
    }





    private void nothingSelected() {
        // Nothing selected.
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No Chat Selected");
        alert.setContentText("Please select a chat in the table.");

        alert.showAndWait();
    }
}
