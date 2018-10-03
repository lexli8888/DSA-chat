package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controller.ChatOverviewController;
import sample.controller.PersonEditDialogController;
import sample.controller.PersonOverviewController;
import sample.controller.RootLayoutController;
import sample.model.Chat;
import sample.model.Message;
import sample.model.Person;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private ObservableList<Chat> chatsData = FXCollections.observableArrayList();

    public Main() {
        // Add some sample data
        Person muster = new Person("Hans", "Muster");
        Person mueller = new Person("Ruth", "Mueller");
        Person kurz = new Person("Heinz", "Kurz");
        Person meier = new Person("Cornelia", "Meier");
        Person meyer = new Person("Werner", "Meyer");
        Person kunz = new Person("Lydia", "Kunz");
        Person best = new Person("Anna", "Best");
        Person mayer = new Person("Stefan", "Mayer");
        Person müller = new Person("Martin", "Müller");

        // my name's mueller, i'am chatting with kurz
        Message message1 = new Message("hey", mueller);
        Message message2 = new Message("hey", kurz);
        Message message3 = new Message("how are you?", mueller);
        Message message4 = new Message("good", kurz);

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        messages.add(message4);

        Chat chat1 = new Chat("Test1",kurz,messages);

        personData.add(muster);
        personData.add(mueller);
        personData.add(kurz);
        personData.add(meier);
        personData.add(meyer);
        personData.add(kunz);
        personData.add(best);
        personData.add(mayer);
        personData.add(müller);

        chatsData.add(chat1);
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    public ObservableList<Chat> getChatsData() {
        return chatsData;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ChatApp");

        initRootLayout();
        //showPersonOverview();
        showChatOverview();
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showChatOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ChatOverview.fxml"));
            AnchorPane chatOverview = (AnchorPane) loader.load();

            // Set chat overview into the center of root layout.
            rootLayout.setCenter(chatOverview);

            // Give the controller access to the main app.
            ChatOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showPersonEditDialog(Person person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
