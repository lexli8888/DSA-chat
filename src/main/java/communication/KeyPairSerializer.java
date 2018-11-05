package communication;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.security.KeyPair;

public class KeyPairSerializer extends JsonSerializer<KeyPair> {

    @Override
    public void serialize(KeyPair keyPair, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeBinaryField("private", keyPair.getPrivate().getEncoded());
        jsonGenerator.writeBinaryField("public", keyPair.getPublic().getEncoded());
        jsonGenerator.writeEndObject();
    }
}
