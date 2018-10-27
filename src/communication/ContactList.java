package communication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.util.ArrayList;
import java.util.List;

public class ContactList {
    private List<UserInfo> contacts;

    public ContactList() {
        contacts = new ArrayList<>();
    }

    public List<UserInfo> getContacts() {
        return contacts;
    }

    public ObservableList<UserInfo> contactsAsObservableList(){
        ObservableList<UserInfo> observableList = FXCollections.observableArrayList();
        observableList.addAll(contacts);
        return observableList;
    }

    public UserInfo searchUser(String username) throws Exception {
        for(UserInfo user : contacts){
            if(user.getUsername() == username){
                return user;
            }
        }
        throw new Exception("User nicht gefunden");
    }

    public void setContacts(List<UserInfo> contacts) {
        this.contacts = contacts;
    }
}
