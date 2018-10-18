package communication;

import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.peers.PeerAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ChatClient {
    private static ISerializationStrategy serializationStrategy = new JsonSerializationStrategy();

    final private PeerDHT peer;

    private KeyPair keyPair;
    private String username;

    public ChatClient() throws IOException {
        this.peer = PeerDHTFactory.CreatePeer();
    }

    public ChatClient(PeerDHT peer) {
        this.peer = peer;
    }

    public String getUsername() {
        return username;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void discoverPeerAddress(PeerAddress address) {
        peer.peer()
                .discover()
                .peerAddress(address)
                .start()
                .awaitUninterruptibly();
    }

    public void discoverClient(ChatClient client) {
        discoverPeerAddress(client.peer.peerAddress());
    }

    public void discoverOnLocalhost(int port) throws UnknownHostException {
        discoverOnInet("127.0.0.1", port);
    }

    public void discoverOnInet(String host, int port) throws UnknownHostException {
        FutureBootstrap fb = this.peer.peer().
                bootstrap().
                inetAddress(InetAddress.getByName(host)).
                ports(port).
                start();

        fb.awaitUninterruptibly();
        if (fb.isSuccess()) {
            discoverPeerAddress(fb.bootstrapTo().iterator().next());
        }
    }

    public void discoverOnCloud() throws UnknownHostException {
        discoverOnInet("p2p.apps.bertschi.io", 4000);
    }

    public boolean sendMessage(ChatInfo chat, ChatMessage message) throws Exception {
        String locationKey = chat.getId() + "-messages";
        String contentKey = message.getId();
        String data = serializationStrategy.serialize(message);

        return PeerDHTUtil.putEncryptedData(peer, keyPair, chat.getKeyPair().getPublic(),
                locationKey, contentKey, data, 60 * 60 * 24 * 7); // 7 days
    }

    public List<ChatMessage> getMessages(ChatInfo chat) throws Exception {
        String locationKey = chat.getId() + "-messages";
        List<String> data = PeerDHTUtil.getEncryptedData(peer, chat.getKeyPair(), locationKey);
        List<ChatMessage> messages = new ArrayList<>();
        for(String item : data) {
            messages.add(serializationStrategy.deserialize(item, null, ChatMessage.class));
        }
        return messages;
    }

    public boolean setOnlineStatus(String status) throws IOException {
        return PeerDHTUtil.putData(this.peer, keyPair, username,
                "onlinestatus", status, 60 * 5); // 5 minutes
    }

    public String getOnlineStatus(String username) throws IOException, ClassNotFoundException {
        return PeerDHTUtil.getData(this.peer, username, "onlinestatus");
    }

    public boolean createChat(ChatInfo chat) throws Exception {
        String data = serializationStrategy.serialize(chat);
        return PeerDHTUtil.putEncryptedData(this.peer, chat.getKeyPair(), chat.getKeyPair().getPublic(),
                chat.getId(), "chat", data, null);
    }

    public boolean inviteChatMember(ChatInfo chat, UserInfo user) throws Exception {
        String locationKey = user.getUsername() + "-invite";
        String data = serializationStrategy.serialize(chat);
        return PeerDHTUtil.putEncryptedData(this.peer, keyPair, user.getPublic(),
                locationKey, UUID.randomUUID().toString(), data, 60 * 60 * 24 * 7); // 7 days
    }

    public List<ChatInfo> getChatInvites() throws Exception {
        String locationKey = username + "-invite";
        List<String> data = PeerDHTUtil.getEncryptedData(this.peer, keyPair, locationKey);
        List<ChatInfo> chats = new ArrayList<>();
        for (String item : data) {
            chats.add(serializationStrategy.deserialize(item, null, ChatInfo.class));
        }
        return chats;
    }

    public ChatList getChatList() throws Exception {
        String data = PeerDHTUtil.getEncryptedData(peer, keyPair, username, "chatlist");
        return serializationStrategy.deserialize(data, new ChatList(), ChatList.class);
    }

    public boolean saveChatList(ChatList chatList) throws Exception {
        String data = serializationStrategy.serialize(chatList);
        return PeerDHTUtil.putEncryptedData(this.peer, keyPair, this.keyPair.getPublic(),
                username, "chatlist", data, null);
    }

    public boolean saveContactList(ContactList contactList) throws Exception {
        String data = serializationStrategy.serialize(contactList);
        return PeerDHTUtil.putEncryptedData(this.peer, keyPair, this.keyPair.getPublic(),
                username, "contactlist", data, null);
    }

    public ContactList getContactList() throws Exception {
        String data = PeerDHTUtil.getEncryptedData(this.peer, keyPair, username, "contactlist");
        return serializationStrategy.deserialize(data, new ContactList(), ContactList.class);
    }

    public void login(String username, KeyPair keyPair) {
        this.username = username;
        this.keyPair = keyPair;
        this.peer.peer().peerBean().keyPair(keyPair);
    }

    public boolean register(UserInfo userInfo, KeyPair keyPair) throws Exception {
        String data = serializationStrategy.serialize(userInfo);

        this.peer.peer().peerBean().keyPair(keyPair);

        boolean result = PeerDHTUtil.putData(peer, keyPair, userInfo.getUsername(), "user", data, null);
        if (result) {
            this.username = userInfo.getUsername();
            this.keyPair = keyPair;
        }
        return result;
    }

    public UserInfo getUserInfo(String username) throws Exception {
        String data = PeerDHTUtil.getData(this.peer, username, "user");
        return serializationStrategy.deserialize(data, null, UserInfo.class);
    }
}