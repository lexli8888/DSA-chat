package gui.controller;

import gui.Main;
import gui.state.DataState;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AddNotariatFileController implements IDataStateModalController {

    private Main mainapp;
    private DataState dataState;
    private Stage dialogStage;
    private boolean isOkClicked = false;

    @FXML
    public void handleDatei() {

    }

    @FXML
    public void handleBrowse() {
    }

    @Override
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
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
}
