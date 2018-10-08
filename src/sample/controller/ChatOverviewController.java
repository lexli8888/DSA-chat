package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import sample.Main;
import sample.model.Chat;

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

    private Main mainApp;

    public ChatOverviewController() {
    }

    private void showChatMessages(Chat chat) {
        if (chat != null) {
            // Fill the chat with info from the chat object.
            chatTextArea.setText(chat.getMessages().get(0).getPerson().getFirstName() + "> " + chat.getMessages().get(0).getText());


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
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Chat Selected");
            alert.setContentText("Please select a chat in the table.");

            alert.showAndWait();
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
}
