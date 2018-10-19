package sample.controller;

import communication.ChatClient;
import org.controlsfx.control.CheckComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.Main;
import sample.model.Chat;
import sample.model.Message;
import sample.model.Person;

import java.util.ArrayList;

/**
 * Created by a-003-ebr on 08.10.2018.
 */
public class NewChatDialogController {

    @FXML
    private CheckComboBox memberComboBox;
    @FXML
    private TextField titleField;

    private Stage dialogStage;
    private Chat chat;
    private ChatClient client;
    private Main mainApp;
    private boolean okClicked = false;
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // some Data for dropdown should be the same as in main app
        Person muster = new Person("Hans", "Muster");
        Person mueller = new Person("Ruth", "Mueller");
        Person kurz = new Person("Heinz", "Kurz");
        Person meier = new Person("Cornelia", "Meier");
        Person meyer = new Person("Werner", "Meyer");
        Person kunz = new Person("Lydia", "Kunz");
        Person best = new Person("Anna", "Best");
        Person mayer = new Person("Stefan", "Mayer");
        Person müller = new Person("Martin", "Müller");

        personData.add(muster);
        personData.add(mueller);
        personData.add(kurz);
        personData.add(meier);
        personData.add(meyer);
        personData.add(kunz);
        personData.add(best);
        personData.add(mayer);
        personData.add(müller);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        titleField.setText(chat.getTitle());

        memberComboBox.getItems().addAll(personData);

        memberComboBox.setConverter(new StringConverter<Person>() {
            @Override
            public String toString(Person object) {
                return object.getFirstName();
            }

            @Override
            public Person fromString(String string) {
                for (int i = 0; i < personData.size(); i++) {
                    if (personData.get(i).getFirstName().equals(string)){
                        return personData.get(i);
                    }
                }
                return null;
            }
        });
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            chat.setTitle(titleField.getText());
            chat.addPersons(memberComboBox.getCheckModel().getCheckedItems());

            ObservableList<Message> newMessages = FXCollections.observableArrayList();
            chat.setMessages(newMessages);

            okClicked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title!\n";
        }
        if (memberComboBox.getCheckModel().getCheckedItems() == null ) {
            errorMessage += "No valid selection!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        this.client = mainApp.getChatClient();
    }
}
