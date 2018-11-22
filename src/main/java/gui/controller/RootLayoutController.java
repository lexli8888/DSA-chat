package gui.controller;

import gui.state.DataState;
import gui.util.AlertFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import gui.Main;

import java.io.IOException;

public class RootLayoutController {

    private Main mainApp;
    private DataState dataState;

    public void setMainApp(Main mainApp, DataState dataState)
    {
        this.mainApp = mainApp;
        this.dataState = dataState;
    }

    @FXML
    private void handleExit() throws IOException {
        mainApp.stop();
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
    private void handleShowChatOverview() throws Exception {
        System.out.println("Chat");
        mainApp.showChatOverview();

    }

    @FXML
    private void handleShowAddressBookOverview() throws Exception {
        System.out.println("Address Book");
        mainApp.showPersonOverview();

    }

    @FXML
    private void handleShowChatInvites() throws Exception {
        System.out.println("Chat invites");
        if(dataState.getChatInvites().size() > 0){
            try {
                boolean noMoreInvites = mainApp.showChatInvites();
                if(noMoreInvites){
                    Alert alert = AlertFactory.InformationAlert("Chat Einladung", "Du hast keine neuen Einladungen mehr.");
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.showAndWait();
                }
            } catch (Exception e){
                System.out.println(e.getStackTrace());
            }
        }
        else {
            Alert alert = AlertFactory.InformationAlert("Chat Einladung", "Du hast keine neuen Einladungen mehr.");
            alert.initOwner(mainApp.getPrimaryStage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleShowNotariatService() throws Exception {
        System.out.println("Notariat Service");
        if(mainApp.addNotariatFile(null)){
            mainApp.showNotariat();
        }
    }
}
