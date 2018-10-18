package sample;

import communication.*;
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
import sample.model.Person;
import sample.model.UserSetting;

import java.io.*;
import java.security.KeyPair;
import java.util.Scanner;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private ObservableList<Chat> chatsData = FXCollections.observableArrayList();

    private ContactList contactList;
    private ChatList chatList;
    private ChatClient client;
    private ISerializationStrategy serializationStrategy;
    //private ObservableList<Message> messages = FXCollections.observableArrayList();
    //private ObservableList<Message> messages2 = FXCollections.observableArrayList();

    public Main()  {


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

    public ContactList getContactList() {
        return contactList;
    }

    public ChatList getChatList() { return chatList; }

    public ChatClient getChatClient(){return client;}


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ChatApp");

        serializationStrategy = new JsonSerializationStrategy();

        String path = System.getProperty("user.home") + File.separator + "DSA-Chat";
        File keyFile = new File(path + File.separator + "key.txt");

        if (keyFile.exists()) {
            Scanner sc = new Scanner(keyFile);
            UserSetting userSetting = serializationStrategy.deserialize(sc.nextLine(), null, UserSetting.class);
            client = new ChatClient();
            client.login(userSetting.getUsername(), userSetting.getKeyPair());
            contactList = client.getContactList();
            chatList = client.getChatList();
            initRootLayout();
            showChatOverview();
        } else {
            showStartupDialog();
        }
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
            controller.setClient(client);
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

    @FXML
    public void showStartupDialog(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/StartupDialog.fxml"));
            AnchorPane startupDialog = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(startupDialog);

            StartupDialogController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean addNewContactDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AddNewContact.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            AddNewContactController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

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
            controller.setMainApp(this);
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
