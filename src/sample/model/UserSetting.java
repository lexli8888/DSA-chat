package sample.model;

import java.security.KeyPair;
import java.security.PublicKey;

public class UserSetting {
    private KeyPair keyPair;
    private String username;
    private String firstName;
    private String lastName;

    public UserSetting(){}

    public UserSetting(KeyPair keyPair, String username, String firstName, String lastName) {
        this.keyPair = keyPair;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }



    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
