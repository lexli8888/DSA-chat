import communication.ChatClient;
import communication.ChatInfo;
import communication.ChatList;
import communication.EncryptionUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestChatList {

    @Test
    public void testGetEmptyChatList() throws Exception {
        List<ChatClient> clients = ChatClientTestHelper.getClients(2, true);

        ChatClient client1 = clients.get(0);

        ChatList chatList = client1.getChatList();
        assertEquals(0, chatList.getChats().size());
    }


    @Test
    public void testStoreAndGetContactList() throws Exception {
        List<ChatClient> clients = ChatClientTestHelper.getClients(2, true);

        ChatClient client1 = clients.get(0);

        ChatList chatList = new ChatList();
        List<ChatInfo> chats = new ArrayList<>();
        ChatInfo chat = ChatInfo.New("First chat");
        chats.add(chat);
        chatList.setChats(chats);

        assertTrue(client1.saveChatList(chatList));

        ChatList fetchedChatList = client1.getChatList();

        assertEquals(chatList.getChats().size(), fetchedChatList.getChats().size());

        ChatInfo fetchedChat = fetchedChatList.getChats().get(0);
        assertEquals(chat.getId(), fetchedChat.getId());
        assertEquals(chat.getTitle(), fetchedChat.getTitle());
        assertEquals(EncryptionUtil.getKeySignature(chat.getKeyPair().getPrivate()), EncryptionUtil.getKeySignature(fetchedChat.getKeyPair().getPrivate()));
        assertEquals(EncryptionUtil.getKeySignature(chat.getKeyPair().getPublic()), EncryptionUtil.getKeySignature(fetchedChat.getKeyPair().getPublic()));
    }
}
