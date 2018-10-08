package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import sample.Main;
import sample.model.Chat;
import sample.model.Message;
import sample.model.Person;

/**
 * Created by a-003-ebr on 01.10.2018.
 */
public class ChatOverviewController {

    @FXML
    private TableView<Chat> chatsTable;
    @FXML
    private TableColumn<Chat, String> chatsColumn;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextArea inputTextArea;

    private Main mainApp;

    public ChatOverviewController() {
    }

    private void showChatMessages(Chat chat) {
        if (chat != null && chat.getMessages() != null) {
            chatTextArea.clear();
            // Fill the chat with info from the chat object.
            for (int i = 0; i < chat.getMessages().size(); i++){
                chatTextArea.appendText(chat.getMessages().get(i).getPerson().getFirstName() + "> " + chat.getMessages().get(i).getText());
                chatTextArea.appendText("\n");
            }
        } else {
            // chat is null, remove all the text.
            chatTextArea.setText("");
        }
    }

    @FXML
    private void handleNewChat(){

    }

    @FXML
    private void handleDeleteChat() {
        int selectedIndex = chatsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            chatsTable.getItems().remove(selectedIndex);
        } else {
            nothingSelected();
        }
    }

    @FXML
    private void handleNewChat() {
        Chat tempChat = new Chat();
        boolean okClicked = mainApp.showNewChatDialog(tempChat);
        if (okClicked) {
            mainApp.getChatsData().add(tempChat);
        }
    }

    @FXML
    private void handleNewChatEntry() {
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
                // I am Ruth Mueller -> logged in user
                Person mueller = new Person("Ruth", "Mueller");
                Message msg = new Message(inputTextArea.getText(), mueller);
                mainApp.getChatsData().get(selectedIndex).getMessages().add(msg);

                chatTextArea.appendText(msg.getPerson().getFirstName() + "> " + msg.getText() + "\n");
                inputTextArea.clear();
            }
        }

    @FXML
    private void initialize() {
        // Initialize the chat table with the two columns.
        chatsColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        chatsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showChatMessages(newValue));
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        chatsTable.setItems(mainApp.getChatsData());

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
