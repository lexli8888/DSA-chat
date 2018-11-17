package gui.model;

import communication.UserInfo;

import java.security.KeyPair;

public class UserSetting {
    private KeyPair keyPair;
    private String username;
    private String firstName;
    private String lastName;
    private String walletPath;
    private String walletPassword;
    private String notaryAddress;

    public UserSetting(){}

    public UserSetting(KeyPair keyPair, String username, String firstName, String lastName, String walletPath, String walletPassword) {
        this.keyPair = keyPair;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.walletPath = walletPath;
        this.walletPassword = walletPassword;
    }

    public UserInfo toUserInfo() {
        return UserInfo.New(keyPair.getPublic(), username, firstName, lastName, notaryAddress);
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

    public String getWalletPath() {
        return walletPath;
    }

    public void setWalletPath(String walletPath) {
        this.walletPath = walletPath;
    }

    public String getWalletPassword() {
        return walletPassword;
    }

    public void setWalletPassword(String walletPassword) {
        this.walletPassword = walletPassword;
    }

    public String getNotaryAddress() {
        return notaryAddress;
    }

    public void setNotaryAddress(String notaryAddress) {
        this.notaryAddress = notaryAddress;
    }
}
