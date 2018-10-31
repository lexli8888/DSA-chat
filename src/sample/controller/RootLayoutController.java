package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import sample.Main;

import java.io.IOException;

public class RootLayoutController {

    private Main mainApp;

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
    private void handleShowChatOverview() throws IOException {
        System.out.println("Chat");
        mainApp.showChatOverview();

    }

    @FXML
    private void handleShowAddressBookOverview() throws IOException {
        System.out.println("Address Book");
        mainApp.showPersonOverview();

    }



}
