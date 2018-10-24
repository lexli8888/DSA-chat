package communication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.StringProperty;

import java.security.*;
import java.util.Base64;

public class UserInfo {
    private PublicKey publicKey;
    private String username;
    private String firstName;
    private String lastName;

    public UserInfo() {

    }

    public static UserInfo New(PublicKey publicKey, String username, String firstName, String lastName) throws NoSuchAlgorithmException {
        UserInfo info = new UserInfo();
        info.publicKey = publicKey;
        info.username = username;
        info.firstName = firstName;
        info.lastName = lastName;
        return info;
    }

    @JsonIgnore
    public String getPublicKeySignature() throws NoSuchAlgorithmException {
        return EncryptionUtil.getKeySignature(publicKey);
    }

    public boolean matchesPublicKey(Key publicKey) throws NoSuchAlgorithmException {
        String signature = EncryptionUtil.getKeySignature(publicKey);
        return signature.equals(this.getPublicKeySignature());
    }

    public PublicKey getPublic() {
        return publicKey;
    }

    public void setPublic(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}