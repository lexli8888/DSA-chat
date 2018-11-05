package gui.controller;

import javafx.fxml.FXML;
import gui.Main;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import gui.state.DataState;

import java.io.IOException;

public class StartupDialogController implements IDataStateController {

    private Main mainApp;
    private DataState dataState;

    public StartupDialogController() throws IOException {
    }

    @FXML
    private TextField FirstName;

    @FXML
    private TextField LastName;

    @FXML
    private TextField UserName;

    @FXML
    private Label ErrorMsg;

    @FXML
    void handleStartup() throws Exception {

        String firstName = FirstName.getText();
        String lastName = LastName.getText();
        String userName = UserName.getText();


        if (!userName.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
            dataState.register(userName, firstName, lastName);
        } else {
            ErrorMsg.setText("Bitte Benutzername, Vorname und Nachname angeben");
        }

        try {
            mainApp.start(mainApp.getPrimaryStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setState(Main mainApp, DataState state) {
        this.dataState = state;
        this.mainApp = mainApp;
    }
}


