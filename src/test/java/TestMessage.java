import communication.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMessage {

    @Test
    public void testCreateChatInviteAndSendMessage() throws Exception {
        List<ChatClient> clients = ChatClientTestHelper.getClients(2, true);

        ChatClient client1 = clients.get(0);
        ChatClient client2 = clients.get(1);

        ChatInfo chatInfo = ChatInfo.New("test");
        assertTrue(client1.createChat(chatInfo));

        UserInfo userInfo2 = client1.getUserInfo(client2.getUsername());
        assertNotNull(userInfo2);

        assertTrue(client1.inviteChatMember(chatInfo, userInfo2));

        List<ChatInfo> invites = client2.getChatInvites();
        assertEquals(1, invites.size());

        assertEquals(chatInfo.getTitle(), invites.get(0).getTitle());
        assertEquals(chatInfo.getId(), invites.get(0).getId());
        assertEquals(EncryptionUtil.getKeySignature(chatInfo.getKeyPair().getPublic()), EncryptionUtil.getKeySignature(invites.get(0).getKeyPair().getPublic()));
        assertEquals(EncryptionUtil.getKeySignature(chatInfo.getKeyPair().getPrivate()), EncryptionUtil.getKeySignature(invites.get(0).getKeyPair().getPrivate()));

        assertTrue(client2.sendMessage(chatInfo, ChatMessage.New(userInfo2, "hello world")));

        List<ChatMessage> messages = client1.getMessages(chatInfo);
        assertEquals(1, messages.size());
        assertEquals("hello world", messages.get(0).getText());
    }

}
