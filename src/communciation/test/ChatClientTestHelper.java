package communciation.test;

import communication.ChatClient;
import communication.ChatClientFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;

public class ChatClientTestHelper {
    public static List<ChatClient> getClients(int amount) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        List<ChatClient> clients = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            KeyPair key = keyGenerator.generateKeyPair();
            ChatClient client = ChatClientFactory.CreateNewClient(key);
            if (i > 0) {
                client.discoverClient(clients.get(0));
            }
            clients.add(client);
        }

        return clients;
    }
}
