package gui.controller;

import blockchain.NotaryService;
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
import java.math.BigInteger;

public class AddNotariatFileController implements IDataStateModalController {

    final FileChooser fileChooser = new FileChooser();
    private File file = null;
    private Main mainapp;
    private DataState dataState;
    private Stage dialogStage;
    private boolean isOkClicked = false;
    private NotaryService notaryService;

    private UserInfo user;

    @Override
    public void setParams(Object params) {
        if (params != null) {
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
    public void handleDatei() throws Exception {
        if (file != null) {
            if (user == null) {
                try {
                    this.notaryService.store(file);
                } catch (Exception e){
                   e.printStackTrace();
                    Alert alert = AlertFactory.ErrorAlert("Verification", "Die Verifizierung ist fehlgeschlagen, prüfe ob genug Coins vorhanden sind.");
                    alert.initOwner(mainapp.getPrimaryStage());
                    alert.showAndWait();
                }
            } else {
                String notaryAddress = user.getNotaryAddress();
                try {
                    BigInteger timestamp = this.notaryService.verify(file, notaryAddress);
                    if (timestamp.compareTo(BigInteger.valueOf(0)) == 0) {
                        Alert alert = AlertFactory.InformationAlert("Verification failed", "Datei wurde nicht verifiziert");
                        alert.initOwner(mainapp.getPrimaryStage());
                        alert.showAndWait();
                    } else {
                        Alert alert = AlertFactory.InformationAlert("Verification success", "Datei wurde um " + timestamp + " verifiziert");
                        alert.initOwner(mainapp.getPrimaryStage());
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = AlertFactory.ErrorAlert("Verification", "Die Verifizierung ist fehlgeschlagen.");
                    alert.initOwner(mainapp.getPrimaryStage());
                    alert.showAndWait();
                }
            }
            dialogStage.close();
        } else {
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
        try {
            this.notaryService = new NotaryService(state.getUserSetting().getWalletPath(), state.getUserSetting().getWalletPassword());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
