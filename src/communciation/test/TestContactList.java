package communciation.test;

import communication.ChatClient;
import communication.ContactList;
import communication.UserInfo;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestContactList {

    @Test
    public void testGetEmptyContactList() throws Exception {
        List<ChatClient> clients = ChatClientTestHelper.getClients(2);

        ChatClient client1 = clients.get(0);

        ContactList contactList = client1.getContactList();
        assertEquals(0, contactList.getContactsAsList().size());
    }


    @Test
    public void testStoreAndGetContactList() throws Exception {
        List<ChatClient> clients = ChatClientTestHelper.getClients(2);


        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        ChatClient client1 = clients.get(0);

        ContactList contactList = new ContactList();
        List<UserInfo> contacts = new ArrayList<>();
        contacts.add(UserInfo.New(keyPair.getPublic(), "alex", "Alexander", "van Schie"));
        contactList.setContacts(contacts);

        assertTrue(client1.saveContactList(contactList));

        ContactList fetchedContactList = client1.getContactList();

        assertEquals(contactList.getContactsAsList().size(), fetchedContactList.getContactsAsList().size());
        assertEquals(contactList.getContactsAsList().get(0).getUsername(), fetchedContactList.getContactsAsList().get(0).getUsername());
        assertEquals(contactList.getContactsAsList().get(0).getFirstName(), fetchedContactList.getContactsAsList().get(0).getFirstName());
        assertEquals(contactList.getContactsAsList().get(0).getLastName(), fetchedContactList.getContactsAsList().get(0).getLastName());
        assertEquals(contactList.getContactsAsList().get(0).getPublicKeySignature(), fetchedContactList.getContactsAsList().get(0).getPublicKeySignature());
    }
}
