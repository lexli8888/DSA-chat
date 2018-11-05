package gui.controller;

import javafx.stage.Stage;

public interface IDataStateModalController extends IDataStateController {
    void setDialogStage(Stage stage);
    boolean isOkClicked();
}
