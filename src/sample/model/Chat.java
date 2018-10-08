package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by a-003-ebr on 01.10.2018.
 */
public class Chat {

    private final StringProperty title;
    private Person person;
    private ObservableList<Message> messages;

    public Chat(){
        this(null,null,null);
    }

    public Chat(String title, Person person, ObservableList<Message> messages) {
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

    public ObservableList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ObservableList<Message> messages) {
        this.messages = messages;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
