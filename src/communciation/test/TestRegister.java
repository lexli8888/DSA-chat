package communciation.test;

import communication.ChatClient;
import communication.UserInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRegister {

    @Test
    public void testRegisterAndGet() throws Exception {
        List<ChatClient> clients = ChatClientTestHelper.getClients(2);

        ChatClient client1 = clients.get(0);
        ChatClient client2 = clients.get(1);

        UserInfo registrationInfo = UserInfo.New(client1.getKeyPair().getPublic(), "pascal", "Pascal", "Bertschi");
        assertTrue(client1.register(registrationInfo));

        UserInfo fetchInfo = client2.getUserInfo("pascal");

        assertEquals(fetchInfo.getUsername(), registrationInfo.getUsername());
        assertEquals(fetchInfo.getFirstName(), registrationInfo.getFirstName());
        assertEquals(fetchInfo.getLastName(), registrationInfo.getLastName());
        assertEquals(fetchInfo.getPublic(), registrationInfo.getPublic());
        assertTrue(fetchInfo.matchesPublicKey(client1.getKeyPair().getPublic()));
    }

    @Test
    public void testConflictingRegister() throws Exception {
        String commonUserName = "user1";
        List<ChatClient> clients = ChatClientTestHelper.getClients(2);

        ChatClient client1 = clients.get(0);
        ChatClient client2 = clients.get(1);

        UserInfo firstRegistrationInfo = UserInfo.New(client1.getKeyPair().getPublic(), commonUserName, "Pascal", "Bertschi");
        assertTrue(client1.register(firstRegistrationInfo));

        UserInfo secondRegistrationInfo = UserInfo.New(client2.getKeyPair().getPublic(), commonUserName, "Alexander", "van Schie");
        assertFalse(client2.register(secondRegistrationInfo));

        UserInfo firstFetchInfo = client1.getUserInfo(commonUserName);
        UserInfo secondFetchInfo = client2.getUserInfo(commonUserName);

        assertEquals(firstFetchInfo.getUsername(), firstRegistrationInfo.getUsername());
        assertEquals(firstFetchInfo.getFirstName(), firstRegistrationInfo.getFirstName());
        assertEquals(firstFetchInfo.getLastName(), firstRegistrationInfo.getLastName());
        assertEquals(firstFetchInfo.getPublic(), firstRegistrationInfo.getPublic());

        assertEquals(secondFetchInfo.getUsername(), firstRegistrationInfo.getUsername());
        assertEquals(secondFetchInfo.getFirstName(), firstRegistrationInfo.getFirstName());
        assertEquals(secondFetchInfo.getLastName(), firstRegistrationInfo.getLastName());
        assertEquals(secondFetchInfo.getPublic(), firstRegistrationInfo.getPublic());
    }
}
