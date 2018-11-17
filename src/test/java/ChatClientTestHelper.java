import communication.ChatClient;
import communication.KeyPairFactory;
import communication.UserInfo;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class ChatClientTestHelper {
    public static List<ChatClient> getClients(int amount, boolean register) throws Exception {
        List<ChatClient> clients = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            KeyPair key = KeyPairFactory.GenerateKeyPair();
            ChatClient client = new ChatClient();
            String username = "user" + i;

            if (i > 0) {
                client.discoverClient(clients.get(0));
            }

            UserInfo info = UserInfo.New(key.getPublic(), username, "first " + i, "last " + i,  "");
            if (register && !client.register(info, key)) {
                throw new Exception("could not register client");
            }

            clients.add(client);
        }

        return clients;
    }
}
