package communication;

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
