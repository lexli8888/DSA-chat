package gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import communication.ChatInfo;
import communication.ChatMessage;
import gui.Main;
import gui.state.DataState;

import java.io.IOException;

public class ChatOverviewController implements IDataStateController {

    @FXML
    private TableView<ChatInfo> chatsTable;
    @FXML
    private TableColumn<ChatInfo, String> chatsColumn;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextArea inputTextArea;

    private Main mainApp;
    private DataState dataState;
    private ObservableList<ChatMessage> messages;
    private ListChangeListener<ChatMessage> messageListChangeListener;

    public ChatOverviewController() {
        messageListChangeListener = new ListChangeListener<ChatMessage>() {
            @Override
            public void onChanged(Change<? extends ChatMessage> c) {
                if (!c.next()) {
                    return;
                }

                if (c.wasAdded()) {
                    chatTextArea.clear();
                    drawMessages();
                }
            }
        };
    }

    private void drawMessages() {
        for (ChatMessage message : messages) {
            chatTextArea.appendText(message.getSender().getUsername() + "> " + message.getText() + "\n");
        }
    }

    private void showChatMessages(ChatInfo chat) throws Exception {
        if (messages != null) {
            messages.removeListener(messageListChangeListener);
        }

        chatTextArea.clear();

        if (chat != null) {
            messages = dataState.getMessages(chat);
            drawMessages();
            messages.addListener(messageListChangeListener);
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

    @FXML
    private void handleDeleteChat() throws Exception {
        int selectedIndex = chatsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            ChatInfo chat = chatsTable.getItems().get(selectedIndex);
            dataState.removeChat(chat);
        } else {
            nothingSelected();
        }
    }

    @FXML
    private void handleNewChat() throws Exception {
        boolean okClicked = mainApp.addNewChatDialog();
        if (okClicked) {
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
            if (!dataState.sendMessage(chat, inputTextArea.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Message Warning");
                alert.setHeaderText("Message not sent");
                alert.setContentText("Houston, we have a problem. Check your Network settings and retry to send the message.");

                alert.showAndWait();
            } else {
                inputTextArea.clear();
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

    @Override
    public void setState(Main mainApp, DataState state) {
        this.mainApp = mainApp;
        this.dataState = state;

        chatsTable.setItems(state.getChats());

        handleChatInvites();

    }

    public void handleChatInvites(){
        if(dataState.getChatInvites().size() > 0){
            try {
                boolean noMoreInvites = mainApp.showChatInvites();
                if(noMoreInvites){
                    chatsTable.setItems(dataState.getChats());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.setTitle("Chat Einladungne");
                    alert.setHeaderText("Keine neuen Einladungen vorhanden");
                    alert.setContentText("Du hast keine neuen Einladungen mehr.");

                    alert.showAndWait();
                }
            } catch (Exception e){
                System.out.println(e.getStackTrace());
            }

        }
    }
}
