package communication;

import java.util.Date;
import java.util.UUID;

public class ChatMessage {
    private String id;
    private UserInfo sender;
    private String text;
    private Date date;

    public static ChatMessage New(UserInfo sender, String text) {
        ChatMessage message = new ChatMessage();
        message.setId(UUID.randomUUID().toString());
        message.setSender(sender);
        message.setText(text);
        message.setDate(new Date());
        return message;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
