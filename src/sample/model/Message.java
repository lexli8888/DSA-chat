package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by a-003-ebr on 03.10.2018.
 */
public class Message {

    private final StringProperty text;
    private final Person person;

    public Message() { this(null, null); }

    public Message(String text, Person person) {
        this.text = new SimpleStringProperty(text);;
        this.person = person;
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public Person getPerson() {
        return person;
    }
}
