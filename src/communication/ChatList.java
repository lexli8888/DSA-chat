package communication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ChatList {
    private List<ChatInfo> chats;

    public ChatList() {
        chats = new ArrayList<>();
    }

    public List<ChatInfo> getChats() {
        return chats;
    }

    public ObservableList<ChatInfo> chatsAsObservableList(){
        ObservableList<ChatInfo> observableList = FXCollections.observableArrayList();
        observableList.addAll(chats);
        return observableList;
    }

    public void setChats(List<ChatInfo> chats) {
        this.chats = chats;
    }

}
