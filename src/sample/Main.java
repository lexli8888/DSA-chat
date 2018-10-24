package sample;

import communciation.test.UserInfoFactory;
import communication.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controller.*;
import sample.model.UserSetting;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ContactList contactList;
    private ChatList chatList;
    private ChatClient client;
    private ISerializationStrategy serializationStrategy;

    public Main() throws Exception {
        client = new ChatClient();
    }

    public ContactList getContactList() { return contactList; }

    public ChatList getChatList() { return chatList; }

    public ChatClient getChatClient(){return client;}

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ChatApp");

        serializationStrategy = new JsonSerializationStrategy();

        String APP_DATA_PATH = System.getProperty("user.home") + File.separator + "DSA-Chat";
        File keyFile = new File(APP_DATA_PATH + File.separator + "key.txt");

        if (keyFile.exists()) {
            Scanner sc = new Scanner(keyFile);
            UserSetting userSetting = serializationStrategy.deserialize(sc.nextLine(), null, UserSetting.class);
            client.login(userSetting.getUsername(), userSetting.getKeyPair());
            fetchUserData();
            initRootLayout();
            showChatOverview();
        } else {
            showStartupDialog();
        }
    }

    private void fetchUserData() throws Exception {
        contactList = client.getContactList();
        chatList = client.getChatList();
        generateTestUsers(5);
    }

    private void generateTestUsers(int amount) throws Exception {
        List<UserInfo> list = contactList.getContactsAsList();
        list.addAll(UserInfoFactory.getUsers(amount));
        contactList.setContacts(list);
        client.saveContactList(contactList);
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

        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewChatDialog(){
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
            AddNewChatController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);

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
