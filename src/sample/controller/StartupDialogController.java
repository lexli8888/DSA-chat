package sample.controller;

import communication.*;
import javafx.fxml.FXML;
import sample.Main;
import sample.model.Person;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import sample.model.UserSetting;
import sun.security.rsa.RSAPublicKeyImpl;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;

public class StartupDialogController {

    private ISerializationStrategy serializationStrategy;

    private Main mainApp;

    private ChatClient client = new ChatClient();

    public StartupDialogController() throws IOException {}

    @FXML
    private TextField FirstName;

    @FXML
    private TextField LastName;

    @FXML
    private TextField UserName;

    @FXML
    private Label ErrorMsg;


    @FXML
    private void initialize() {
        serializationStrategy = new JsonSerializationStrategy();
    }


    @FXML
    void handleStartup() throws Exception {

        String firstName = FirstName.getText();
        String lastName = LastName.getText();
        String userName = UserName.getText();

        if(!userName.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()){
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = generator.generateKeyPair();

            UserInfo user = UserInfo.New(keyPair.getPublic(), userName, firstName, lastName);
            if (client.register(user, keyPair)) {
                createKeyFile(keyPair, userName, firstName, lastName);
            } else {
                ErrorMsg.setText("Benutzername vergeben");
            }
        } else {
            ErrorMsg.setText("Bitte Vorname und Nachname angeben");
        }

        try {
            mainApp.start(mainApp.getPrimaryStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createKeyFile(KeyPair keyPair, String username, String firstname, String lastname) {
            UserSetting user = new UserSetting(keyPair, username, firstname, lastname);

            String path = System.getProperty("user.home") + File.separator + "DSA-Chat";
            File customDir = new File(path);

            customDir.mkdirs();
            try {
                File keyFile = new File(path + File.separator + "key.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(keyFile));
                writer.write(serializationStrategy.serialize(user));
                writer.close();
                keyFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        System.out.println(customDir + " was created");

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        this.client = mainApp.getChatClient();
    }
}


