package communciation.test;

import communication.ChatClient;
import communication.ContactList;
import communication.UserInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestContactList {

    @Test
    public void testGetEmptyContactList() throws Exception {
        List<ChatClient> clients = ChatClientTestHelper.getClients(2);

        ChatClient client1 = clients.get(0);

        ContactList contactList = client1.getContactList("pascal", client1.getKeyPair());
        assertEquals(0, contactList.getContacts().size());
    }


    @Test
    public void testStoreAndGetContactList() throws Exception {
        List<ChatClient> clients = ChatClientTestHelper.getClients(2);
        String username = "elias";
        ChatClient client1 = clients.get(0);

        ContactList contactList = new ContactList();
        List<UserInfo> contacts = new ArrayList<>();
        contacts.add(UserInfo.New("alex", "Alexander", "van Schie"));
        contactList.setContacts(contacts);

        assertTrue(client1.saveContactList(username, client1.getKeyPair(), contactList));

        ContactList fetchedContactList = client1.getContactList(username, client1.getKeyPair());

        assertEquals(contactList.getContacts().size(), fetchedContactList.getContacts().size());
    }
}
