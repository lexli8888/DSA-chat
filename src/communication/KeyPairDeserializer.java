package communication;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyPairDeserializer extends JsonDeserializer<KeyPair> {

    @Override
    public KeyPair deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        byte[] privateKeyData = node.get("private").binaryValue();
        byte[] publicKeyData = node.get("public").binaryValue();

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyData));
            PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyData));

            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new IOException("invalid key pair");
        }
    }
}
