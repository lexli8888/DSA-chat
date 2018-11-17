package gui.controller;

import blockchain.NotaryService;
import gui.Main;
import gui.state.DataState;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.web3j.crypto.CipherException;

import java.io.File;
import java.io.IOException;

public class AddNotariatFileController implements IDataStateModalController {

    private Main mainApp;
    private DataState dataState;
    private Stage dialogStage;
    private boolean isOkClicked = false;
    private NotaryService notaryService;

    @FXML
    private TextField infoField;

    public AddNotariatFileController() throws IOException, CipherException {
        //TODO can you please review this. I don't get it
        notaryService = new NotaryService(dataState.getUser().getWalletPath(), dataState.getUser().getWalletPassword());
    }

    @FXML
    public void handleVerify() throws Exception {
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
            //TODO add notaryService.verify here (which address)? userInfo.notaryAddress?
            notaryService.verify(file, "some Address");
        }
    }

    @FXML
    public void handleSend() throws Exception {
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
            notaryService.store(file);
            infoField.setText("Datei gesendet");
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
        this.mainApp = mainApp;
        this.dataState = state;
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("open File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
    }
}
