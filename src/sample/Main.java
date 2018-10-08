package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controller.*;
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
    //private ObservableList<Message> messages = FXCollections.observableArrayList();
    //private ObservableList<Message> messages2 = FXCollections.observableArrayList();

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

        /*
        // my name's mueller, i'am chatting with kurz
        Message message11 = new Message("hey", mueller);
        Message message12 = new Message("hey", kurz);
        Message message13 = new Message("how are you?", mueller);
        Message message14 = new Message("good", kurz);

        // my name's mueller, i'am chatting with kurz
        Message message21 = new Message("hey", muster);
        Message message22 = new Message("moin", mayer);
        Message message23 = new Message("Wie geht's so?", muster);
        Message message24 = new Message("gut", mayer);

        messages.add(message11);
        messages.add(message12);
        messages.add(message13);
        messages.add(message14);

        messages2.add(message21);
        messages2.add(message22);
        messages2.add(message23);
        messages2.add(message24);

        Chat chat1 = new Chat("Test1",kurz,messages);
        Chat chat2 = new Chat("Test2",best,messages2);
        */

        personData.add(muster);
        personData.add(mueller);
        personData.add(kurz);
        personData.add(meier);
        personData.add(meyer);
        personData.add(kunz);
        personData.add(best);
        personData.add(mayer);
        personData.add(müller);

        /*
        chatsData.add(chat1);
        chatsData.add(chat2);
        */
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
            primaryStage.setResizable(false);
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

    public boolean showNewChatDialog(Chat chat){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ChatEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Chat");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the chat into the controller and Give the controller access to the main app.
            NewChatDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            //controller.setMainApp(this);
            controller.setChat(chat);


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
