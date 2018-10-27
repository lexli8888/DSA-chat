package communciation.test;

import communication.ChatClient;
import communication.UserInfo;

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
            ChatClient client = new ChatClient();
            String username = "user" + i;

            if (i > 0) {
                client.discoverClient(clients.get(0));
            }

            UserInfo info = UserInfo.New(key.getPublic(), username, "first " + i, "last " + i);
            if (!client.register(info, key)) {
                throw new Exception("could not register client");
            }

            clients.add(client);
        }

        return clients;
    }
}
