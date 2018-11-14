package gui.controller;

import gui.Main;
import gui.state.DataState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class NotariatController implements IDataStateModalController {

    private Main mainapp;
    private DataState dataState;
    private Stage dialogstage;
    private boolean isOkClicked = false;

    @Override
    public void setDialogStage(Stage stage) {
        this.dialogstage = stage;
    }

    @Override
    public boolean isOkClicked() {
       return isOkClicked;
    }

    @Override
    public void setState(Main mainApp, DataState state) throws Exception {
        this.mainapp = mainApp;
        this.dataState = state;
    }

    @FXML
    public void handleOk() {
        isOkClicked = true;
        dialogstage.close();
    }
}
