package communication;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

public class EncryptionUtil {
    public static byte[] getRandomBytes(int amount) {
        SecureRandom rnd = new SecureRandom();
        byte bytes[] = new byte[amount];
        rnd.nextBytes(bytes);
        return bytes;
    }

    public static String decryptData(PrivateKey key, String data) throws Exception {
        int byteLength = 128;

        byte[] content = Base64.getDecoder().decode(data);

        byte[] encryptedIv = new byte[byteLength];
        byte[] encryptedKey = new byte[byteLength];
        byte[] encryptedData = new byte[content.length - encryptedIv.length - encryptedKey.length];

        System.arraycopy(content, 0, encryptedIv, 0, encryptedIv.length);
        System.arraycopy(content, encryptedIv.length, encryptedKey, 0, encryptedKey.length);
        System.arraycopy(content, encryptedIv.length + encryptedKey.length, encryptedData, 0, encryptedData.length);

        byte[] decryptedIv = decryptRSA(key, encryptedIv);
        byte[] decryptedKey = decryptRSA(key, encryptedKey);
        byte[] decryptedData = decryptAES(decryptedKey, decryptedIv, encryptedData);

        return new String(decryptedData);
    }

    public static String encryptData(PublicKey key, String data) throws Exception {
        int byteLength = 16;

        byte[] aesIv = getRandomBytes(byteLength);
        byte[] aesKey = getRandomBytes(byteLength);

        byte[] encryptedIv = encryptRSA(key, aesIv);
        byte[] encryptedKey = encryptRSA(key, aesKey);
        byte[] encryptedData = encryptAES(aesKey, aesIv, data.getBytes("UTF-8"));

        byte[] result = new byte[encryptedIv.length + encryptedKey.length + encryptedData.length];
        System.arraycopy(encryptedIv, 0, result, 0, encryptedIv.length);
        System.arraycopy(encryptedKey, 0, result, encryptedIv.length, encryptedKey.length);
        System.arraycopy(encryptedData, 0, result, encryptedIv.length + encryptedKey.length, encryptedData.length);

        return Base64.getEncoder().encodeToString(result);
    }

    public static byte[] encryptRSA(PublicKey key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    public static byte[] decryptRSA(PrivateKey key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    public static byte[] encryptAES(byte[] keyData, byte[] ivData, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        IvParameterSpec iv = new IvParameterSpec(ivData);
        SecretKeySpec skeySpec = new SecretKeySpec(keyData, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        return cipher.doFinal(data);
    }

    public static byte[] decryptAES(byte[] keyData, byte[] ivData, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        IvParameterSpec iv = new IvParameterSpec(ivData);
        SecretKeySpec skeySpec = new SecretKeySpec(keyData, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        return cipher.doFinal(data);
    }

    public static String getKeySignature(Key key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getEncoded());
        byte[] data = md.digest();

        Base64.Encoder b64Encoder = Base64.getEncoder().withoutPadding();
        return b64Encoder.encodeToString(data);
    }
}
