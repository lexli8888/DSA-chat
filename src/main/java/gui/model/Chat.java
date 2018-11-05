package gui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Created by a-003-ebr on 01.10.2018.
 */
public class Chat {

    private final StringProperty title;
    private ObservableList<Person> persons;
    private ObservableList<Message> messages;

    public Chat(){
        this(null,null,null);
    }

    public Chat(String title, ObservableList<Person> members, ObservableList<Message> messages) {
        this.title = new SimpleStringProperty(title);
        this.persons = members;
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

    public ObservableList<Person> getPersons() {
        return persons;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
    }

    public void addPersons(ObservableList<Person> persons){
       this.persons.addAll(persons);
    }
}
