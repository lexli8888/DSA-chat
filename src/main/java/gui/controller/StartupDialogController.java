package gui.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import gui.Main;

import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import gui.state.DataState;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StartupDialogController implements IDataStateController {

    private Main mainApp;
    private DataState dataState;
    private String walletPath = "";
    private String notaryAddress = "";

    public StartupDialogController() throws IOException {
    }

    @FXML
    private TextField FirstName;

    @FXML
    private TextField LastName;

    @FXML
    private TextField UserName;

    @FXML
    private PasswordField WalletPassword;

    @FXML
    private Label ErrorMsg;

    @FXML
    void handleStartup() throws Exception {

        String firstName = FirstName.getText();
        String lastName = LastName.getText();
        String userName = UserName.getText();
        String walletPassword = WalletPassword.getText();
        if (!userName.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !walletPath.isEmpty() && !walletPassword.isEmpty())  {

            boolean success = dataState.register(userName, firstName, lastName, walletPath, walletPassword, notaryAddress);
            if (!success){
                ErrorMsg.setText("Bei der Registrierung ist etwas schief gelaufen.");
            } else {
                try {
                    mainApp.start(mainApp.getPrimaryStage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            ErrorMsg.setText("Bitte Formular vollständig ausfüllen.");
        }

    }

    @FXML
    void chooseWalletFile(){
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
            setWalletPath(file.getAbsolutePath());
            getNotaryAddressFromWallet();
        }
    }


    @Override
    public void setState(Main mainApp, DataState state) {
        this.dataState = state;
        this.mainApp = mainApp;
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Wallet öffnen");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
    }

    private void getNotaryAddressFromWallet() {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(walletPath));
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode address = rootNode.path("address");

            setNotaryAddress(address.asText());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setWalletPath(String walletPath) {
        this.walletPath = walletPath;
    }

    public void setNotaryAddress(String notaryAddress) {
        this.notaryAddress = notaryAddress;
    }
}


