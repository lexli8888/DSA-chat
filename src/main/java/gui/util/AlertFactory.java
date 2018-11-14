package gui.util;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

public class AlertFactory {

    public static Alert InformationAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title + " Information");
        alert.setContentText(content);
        return alert;
    }

    public static Alert WarningAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title + " Warning");
        alert.setContentText(content);
        return alert;
    }

    public static Alert ErrorAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title + " Error");
        alert.setContentText(content);
        return alert;
    }

}
