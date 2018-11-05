package gui.controller;

import communication.ChatInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import gui.Main;
import gui.state.DataState;


public class ChatInvitesController implements IDataStateModalController {

    @FXML
    private TableView<ChatInfo> invitesTable;
    @FXML
    private TableColumn<ChatInfo, String> invitesColumn;

    private Stage dialogStage;
    private DataState dataState;

    private Main main;

    private boolean okClicked = false;

    @FXML
    private void initialize() throws Exception {
        invitesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
    }

    @Override
    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    @Override
    public boolean isOkClicked() {
        return okClicked;
    }

    @Override
    public void setState(Main mainApp, DataState state) {
        this.dataState = state;
        this.main = mainApp;

        invitesTable.setItems(dataState.getChatInvites());
    }

    public void handleAccept() throws Exception {
       handleSelectedItem(true);
       checkRemainingInvites();
    }

    public void handleDecline() throws Exception {
        handleSelectedItem(false);
        checkRemainingInvites();
    }

    private void nothingSelected() {
        // Nothing selected.
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection");
        alert.setHeaderText("No Chat Selected");
        alert.setContentText("Please select a chat in the table.");

        alert.showAndWait();
    }

    private void handleSelectedItem(boolean addToChatList) throws Exception {
        int selectedIndex = invitesTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex < 0){
            nothingSelected();
        }
        else {
            ChatInfo chat = invitesTable.getSelectionModel().getSelectedItem();
            if(addToChatList){
                dataState.addChat(chat);
            }
            dataState.removeInvite(chat);
            invitesTable.setItems(dataState.getChatInvites());
        }
    }

    private void checkRemainingInvites(){
        if(dataState.getChatInvites().size() == 0){
            okClicked = true;
            dialogStage.close();
        }
    }


}
