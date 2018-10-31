package communciation.test;

import communication.EncryptionUtil;
import communication.KeyPairFactory;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEncryptionUtil {

    @Test
    public void testRSADeEncryption() throws Exception {
        KeyPair keyPair = KeyPairFactory.GenerateKeyPair();

        byte[] data = EncryptionUtil.getRandomBytes(80);
        byte[] encryptedData = EncryptionUtil.encryptRSA(keyPair.getPublic(), data);
        byte[] decryptedData = EncryptionUtil.decryptRSA(keyPair.getPrivate(), encryptedData);

        assertArrayEquals(decryptedData, data);
    }


    @Test
    public void testAESDeEncryption() throws Exception {
        byte[] iv = EncryptionUtil.getRandomBytes(16);
        byte[] key = EncryptionUtil.getRandomBytes(16);
        byte[] data = EncryptionUtil.getRandomBytes(16);
        byte[] encryptedData = EncryptionUtil.encryptAES(key, iv, data);
        byte[] decryptedData = EncryptionUtil.decryptAES(key, iv, encryptedData);

        assertArrayEquals(decryptedData, data);
    }

    @Test
    public void testDataEncryption() throws Exception {
        KeyPair keyPair = KeyPairFactory.GenerateKeyPair();

        String data = "foo";
        String encryptData = EncryptionUtil.encryptData(keyPair.getPublic(), data);
        String decryptedData = EncryptionUtil.decryptData(keyPair.getPrivate(), encryptData);

        assertEquals(decryptedData, data);
    }
}
