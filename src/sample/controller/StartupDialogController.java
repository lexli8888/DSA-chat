package sample.controller;

import javafx.fxml.FXML;
import sample.Main;
import sample.model.Person;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class StartupDialogController {

    private Main mainApp;

    public StartupDialogController(){};

    @FXML
    private TextField FirstName;

    @FXML
    private TextField LastName;

    @FXML
    private Label ErrorMsg;


    @FXML
    private void initialize() {
    }


    @FXML
    void handleStartup() throws NoSuchAlgorithmException {

        String firstName = FirstName.getText();
        String lastName = LastName.getText();

        if(!firstName.isEmpty() && !lastName.isEmpty()){

            KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
            KeyPair keyPair = generator.generateKeyPair();
            Person user = new Person(FirstName.getText(), LastName.getText(), keyPair.getPublic().toString());

            String path = System.getProperty("user.home") + File.separator + "DSA-Chat";
            File customDir = new File(path);
            File keyFile = new File(path + File.separator + "key.txt");

            createKeyFile(user, customDir, keyFile);

        } else {
            ErrorMsg.setText("Bitte Vorname und Nachname angeben");
        }

        try {
            mainApp.start(mainApp.getPrimaryStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createKeyFile(Person user, File customDir, File keyFile) {
            customDir.mkdirs();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(keyFile));
                writer.write(user.getFirstName());
                writer.write(user.getLastName());
                writer.write(user.getFingerprint());
                writer.close();
                keyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(customDir + " was created");

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;


    }
}


