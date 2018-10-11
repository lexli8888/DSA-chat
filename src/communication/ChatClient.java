package communication;

import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.peers.PeerAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ChatClient {
    private ISerializationStrategy serializationStrategy;

    final private PeerDHT peer;
    final private KeyPair keyPair;

    public ChatClient(PeerDHT peer, KeyPair keyPair) {
        this.peer = peer;
        this.keyPair = keyPair;

        this.serializationStrategy = new JsonSerializationStrategy();
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


    public boolean createChat(ChatInfo chat) throws Exception {
        String data = serializationStrategy.serialize(chat);
        return PeerDHTUtil.putEncryptedData(this.peer, this.keyPair, chat.getKeyPair().getPublic(),
                chat.getId(), "chat", data, null);
    }

    public boolean inviteChatMember(ChatInfo chat, UserInfo user) throws Exception {
        String locationKey = user.getUsername() + "-invite";
        String data = serializationStrategy.serialize(chat);
        return PeerDHTUtil.putEncryptedData(this.peer, this.keyPair, user.getPublic(),
                locationKey, UUID.randomUUID().toString(), data, 60 * 60 * 24 * 7);
    }

    public List<ChatInfo> getChatInvites(String username, KeyPair keyPair) throws Exception {
        String locationKey = username + "-invite";
        List<String> data = PeerDHTUtil.getEncryptedData(this.peer, keyPair, locationKey);
        List<ChatInfo> chats = new ArrayList<>();
        for(String item : data) {
            chats.add(serializationStrategy.deserialize(item, null, ChatInfo.class));
        }
        return chats;
    }

    public ChatList getChatList(String username, KeyPair keyPair) throws Exception {
        String data = PeerDHTUtil.getEncryptedData(this.peer, this.keyPair, username, "chatlist");
        return serializationStrategy.deserialize(data, new ChatList(), ChatList.class);
    }

    public boolean saveChatList(String username, KeyPair keyPair, ChatList chatList) throws Exception {
        String data = serializationStrategy.serialize(chatList);
        return PeerDHTUtil.putEncryptedData(this.peer, this.keyPair, this.keyPair.getPublic(),
                username, "chatlist", data, null);
    }

    public boolean saveContactList(String username, KeyPair keyPair, ContactList contactList) throws Exception {
        String data = serializationStrategy.serialize(contactList);
        return PeerDHTUtil.putEncryptedData(this.peer, this.keyPair, this.keyPair.getPublic(),
                username, "contactlist", data, null);
    }

    public ContactList getContactList(String username, KeyPair keyPair) throws Exception {
        String data = PeerDHTUtil.getEncryptedData(this.peer, this.keyPair, username, "contactlist");
        return serializationStrategy.deserialize(data, new ContactList(), ContactList.class);
    }

    public boolean register(UserInfo userInfo) throws Exception {
        String data = serializationStrategy.serialize(userInfo);
        return PeerDHTUtil.putData(this.peer, this.keyPair, userInfo.getUsername(), "user", data, null);
    }

    public UserInfo getUserInfo(String username) throws Exception {
        String data = PeerDHTUtil.getData(this.peer, username, "user");
        return serializationStrategy.deserialize(data, null, UserInfo.class);
    }
}