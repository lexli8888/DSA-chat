package gui.controller;

import gui.util.AlertFactory;
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

import java.util.Timer;
import java.util.TimerTask;

public class ChatOverviewController implements IDataStateController {

    class RefreshMessages extends TimerTask {
        @Override
        public void run() {
            if(selectedChat != null) {
                try {
                    dataState.loadChatMessages(selectedChat);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private TableView<ChatInfo> chatsTable;
    @FXML
    private TableColumn<ChatInfo, String> chatsColumn;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextArea inputTextArea;

    private Timer messageTimer;
    private Main mainApp;
    private DataState dataState;
    private ChatInfo selectedChat;
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

        messageTimer = new Timer();
        messageTimer.schedule(new RefreshMessages(), 0, 5000);
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

        selectedChat = chat;
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
            Alert alert = AlertFactory.InformationAlert("Success", "Ein neuer Chat wurde erfolgreich erstellt.");
            alert.initOwner(mainApp.getPrimaryStage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewChatEntry() throws Exception {
        int selectedIndex = chatsTable.getSelectionModel().getSelectedIndex();
        if (inputTextArea.getText().isEmpty()) {
            Alert alert = AlertFactory.InformationAlert("No Text", "Please write some text.");
            alert.initOwner(mainApp.getPrimaryStage());
            alert.showAndWait();

        } else if (selectedIndex < 0) {
            nothingSelected();

        } else {
            ChatInfo chat = chatsTable.getSelectionModel().getSelectedItem();
            // Schreibt (auf localhost) zurzeit als Absender noch ein null object, darum können keine Chats verschickt werden.
            if (!dataState.sendMessage(chat, inputTextArea.getText())) {
                Alert alert = AlertFactory.WarningAlert("Message Warning", "Houston, we have a problem. Check your Network settings and retry to send the message.");
                alert.initOwner(mainApp.getPrimaryStage());
                alert.showAndWait();
            } else {
                inputTextArea.clear();
            }
        }
    }


    private void nothingSelected() {
        // Nothing selected.
        Alert alert = AlertFactory.WarningAlert("No Selection", "Please select a chat in the table.");
        alert.initOwner(mainApp.getPrimaryStage());
        alert.showAndWait();
    }

    @Override
    public void setState(Main mainApp, DataState state) throws Exception {
        this.mainApp = mainApp;
        this.dataState = state;

        chatsTable.setItems(state.getChats());

    }

}
