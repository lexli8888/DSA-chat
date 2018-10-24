package communication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ContactList {
    private List<UserInfo> contacts;

    public ContactList() {
        contacts = new ArrayList<>();
    }

    public List<UserInfo> getContactsAsList() {
        return contacts;
    }

    public ObservableList<UserInfo> getContactsAsObservableList(){
        ObservableList<UserInfo> observableList = FXCollections.observableArrayList();
        observableList.addAll(contacts);
        return observableList;
    }

    public void setContacts(List<UserInfo> contacts) {
        this.contacts = contacts;
    }
}
