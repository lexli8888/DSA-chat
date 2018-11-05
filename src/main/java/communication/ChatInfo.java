package communication;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class ChatInfo {
    private String id;
    private String title;
    private KeyPair keyPair;

    public static ChatInfo New(String title, KeyPair keyPair) {
        ChatInfo chat = new ChatInfo();
        chat.setTitle(title);
        chat.setId(UUID.randomUUID().toString());
        chat.setKeyPair(keyPair);
        return chat;
    }

    public static ChatInfo New(String title) throws NoSuchAlgorithmException {
        return ChatInfo.New(title, KeyPairFactory.GenerateKeyPair());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
