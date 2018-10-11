package communication;

public interface ISerializationStrategy {
    <T> T deserialize(String data, T defaultValue, Class<T> dataCls) throws Exception;
    String serialize(Object data) throws Exception;
}
