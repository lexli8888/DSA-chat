package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

/**
 * Created by a-003-ebr on 01.10.2018.
 */
public class Chat {

    private final StringProperty title;
    private final Person person;
    private final ArrayList<Message> messages;

    public Chat(){
        this(null,null,null);
    }

    public Chat(String title, Person person, ArrayList<Message> messages) {
        this.title = new SimpleStringProperty(title);
        this.person = person;
        this.messages = messages;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public Person getPerson() {
        return person;
    }
}
