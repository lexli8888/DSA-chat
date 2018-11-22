package gui.controller;

import communication.SignatureUtil;
import communication.UserInfo;
import gui.Main;
import gui.state.DataState;
import gui.util.AlertFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class AddNotariatFileController implements IDataStateModalController {

    final FileChooser fileChooser = new FileChooser();
    private File file = null;
    private Main mainapp;
    private DataState dataState;
    private Stage dialogStage;
    private boolean isOkClicked = false;

    private UserInfo user;

    @Override
    public void setParams(Object params) {
        System.out.println(params);
        if(params != null) {
            this.user = (UserInfo) params;
            this.btn.setText("Datei verifizieren");
        } else {
            this.btn.setText("Datei signieren");
        }

    }


    @FXML
    private TextField filepath;

    @FXML
    private Button btn;

    @FXML
    public void handleDatei() throws IOException, NoSuchAlgorithmException {
        if(file != null){
           //TODO Add Filesignature
            if(user == null){
                //store
            }
            else{
                String notaryAddress = user.getNotaryAddress();
                //verify
            }
            dialogStage.close();
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
