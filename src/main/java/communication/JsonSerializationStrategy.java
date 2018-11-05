package communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.security.KeyPair;
import java.security.PublicKey;

public class JsonSerializationStrategy implements ISerializationStrategy {
    static ObjectMapper mapper = null;

    private static ObjectMapper getMapper() {
        if(mapper == null) {
            mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(KeyPair.class, new KeyPairSerializer());
            module.addDeserializer(KeyPair.class, new KeyPairDeserializer());
            module.addSerializer(PublicKey.class, new PublicKeySerializer());
            module.addDeserializer(PublicKey.class, new PublicKeyDeserializer());
            mapper.registerModule(module);
        }
        return mapper;
    }

    @Override
    public <T> T deserialize(String data, T defaultValue, Class<T> dataCls) throws Exception {
        if (data == null) {
            return defaultValue;
        }

        return getMapper().readValue(data, dataCls);
    }

    @Override
    public String serialize(Object data) throws Exception {
        return getMapper().writeValueAsString(data);
    }
}
