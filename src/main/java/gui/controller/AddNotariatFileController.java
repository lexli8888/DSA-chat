package gui.controller;

import gui.Main;
import gui.state.DataState;
import gui.util.AlertFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddNotariatFileController implements IDataStateModalController {

    final FileChooser fileChooser = new FileChooser();
    private File file = null;
    private Main mainapp;
    private DataState dataState;
    private Stage dialogStage;
    private boolean isOkClicked = false;

    @FXML
    private TextField filepath;

    @FXML
    public void handleDatei() {
        if(file != null){

        }
        else{
            Alert alert = AlertFactory.ErrorAlert("No file", "Wähle eine Datai aus um fortzufahren!");
            alert.initOwner(mainapp.getPrimaryStage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleBrowse() {
        file = null;
        filepath.clear();
        file = fileChooser.showOpenDialog(mainapp.getPrimaryStage());
        if (file != null) {
           filepath.setText(file.getPath());
        }
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
