package sample.controller;

import communication.ChatClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import sample.Main;

/**
 * Created by a-003-ebr on 01.10.2018.
 */
public class RootLayoutController {

    private Main mainApp;
    private ChatClient client;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ChatApp");
        alert.setHeaderText("About this Application");
        alert.setContentText("Authors: Alexander van Schie, Pascal Bertschi, Elias Brunner");

        alert.showAndWait();
    }

    @FXML
    private void handleShowChatOverview() {
        System.out.println("Chat");
        mainApp.showChatOverview(client);

    }

    @FXML
    private void handleShowAddressBookOverview() {
        System.out.println("Address Book");
        mainApp.showPersonOverview(client);

    }

    public void setClient(ChatClient client) {
        this.client = client;
    }
}
