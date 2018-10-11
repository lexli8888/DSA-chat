package communication;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.security.PublicKey;

public class PublicKeySerializer extends JsonSerializer<PublicKey> {
    @Override
    public void serialize(PublicKey publicKey, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeBinaryField("key", publicKey.getEncoded());
        jsonGenerator.writeEndObject();
    }
}
