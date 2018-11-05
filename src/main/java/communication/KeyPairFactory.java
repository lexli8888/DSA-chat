package communication;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyPairFactory {
    private static KeyPairGenerator keyPairGenerator;

    public static KeyPair GenerateKeyPair() throws NoSuchAlgorithmException {
        if (keyPairGenerator == null) {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
        }

        return keyPairGenerator.generateKeyPair();
    }
}
