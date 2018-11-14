package gui.state;

import com.google.common.collect.Lists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import communication.*;
import gui.model.UserSetting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DataState {
    private static String AppDataPath = System.getProperty("user.home") + File.separator + "DSA-Chat";
    private static String UserSettingPath = AppDataPath + File.separator + "key.txt";

    private ISerializationStrategy serializationStrategy = new JsonSerializationStrategy();

    private UserSetting user;
    private ChatClient client;

    private ObservableList<UserInfo> users = FXCollections.observableArrayList();
    private ObservableList<ChatInfo> chats = FXCollections.observableArrayList();
    private ObservableList<ChatInfo> chatInvites = FXCollections.observableArrayList();
    private HashMap<String, ObservableList<ChatMessage>> messages = new HashMap<>();

    public DataState(ChatClient client) {
        this.client = client;
    }

    public boolean sendMessage(ChatInfo chat, String text) throws Exception {
        ChatMessage message = ChatMessage.New(user.toUserInfo(), text);
        boolean success = client.sendMessage(chat, message);
        if (success) {
            ObservableList<ChatMessage> messages = getMessages(chat);
            messages.add(message);
        }
        return success;
    }

    public ObservableList<ChatMessage> getMessages(ChatInfo chat) throws Exception {
        if (!messages.containsKey(chat.getId())) {
            ObservableList<ChatMessage> observableList = FXCollections.observableArrayList();
            observableList.addAll(client.getMessages(chat));
            messages.put(chat.getId(), observableList);
        }
        return messages.get(chat.getId());
    }

    public boolean register(String userName, String firstName, String lastName) throws Exception {
        KeyPair keyPair = KeyPairFactory.GenerateKeyPair();

        UserInfo user = UserInfo.New(keyPair.getPublic(), userName, firstName, lastName);
        if (client.register(user, keyPair)) {
            createKeyFile(keyPair, userName, firstName, lastName);
            return true;
        }

        return false;
    }

    private void createKeyFile(KeyPair keyPair, String username, String firstname, String lastname) throws Exception {
        UserSetting user = new UserSetting(keyPair, username, firstname, lastname);

        File customDir = new File(AppDataPath);

        customDir.mkdirs();

        File keyFile = new File(UserSettingPath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(keyFile));
        writer.write(serializationStrategy.serialize(user));
        writer.close();
        keyFile.createNewFile();

        this.user = user;

        System.out.println(customDir + " was created");
    }

    private void initData() throws Exception {
        ChatList chatList = client.getChatList();
        chats.addAll(chatList.getChats());

        ContactList contactList = client.getContactList();
        users.addAll(contactList.getContacts());
    }

    public void loadChatMessages(ChatInfo chat) throws Exception {
        List<ChatMessage> newMessages = client.getMessages(chat);
        if (!messages.containsKey(chat.getId())) {
            ObservableList<ChatMessage> observableList = FXCollections.observableArrayList();
            observableList.addAll(newMessages);
            messages.put(chat.getId(), observableList);
        } else {
            ObservableList<ChatMessage> observableList = messages.get(chat.getId());
            observableList.clear();
            observableList.addAll(newMessages);
        }
    }

    public void loadChatInvites() throws Exception {
        chatInvites.clear();

        List<ChatInfo> chatInvitesHistory = client.getChatInvites();
        for (ChatInfo chatInvite : chatInvites) {
            boolean alreadyExists = false;
            for (ChatInfo chat : chats) {
                if (chat.getId().equals(chatInvite.getId())) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                chatInvites.add(chatInvite);
            }
        }
    }

    public boolean init() throws Exception {
        File keyFile = new File(UserSettingPath);

        if (keyFile.exists()) {
            Scanner sc = new Scanner(keyFile);
            user = serializationStrategy.deserialize(sc.nextLine(), null, UserSetting.class);
            sc.close();

            if (!client.login(user.getUsername(), user.getKeyPair())) {
                boolean success = client.register(user.toUserInfo(), user.getKeyPair());
                if (success) {
                    this.initData();
                }
                return success;
            } else {
                this.initData();
                return true;
            }
        }

        return false;
    }

    public void addChat(ChatInfo chat) throws Exception {
        chats.add(chat);

        ChatList chatList = new ChatList();
        chatList.setChats(Lists.newArrayList(chats.iterator()));
        client.saveChatList(chatList);
    }

    public void removeChat(ChatInfo chat) throws Exception {
        chats.remove(chat);

        ChatList chatList = new ChatList();
        chatList.setChats(Lists.newArrayList(chats.iterator()));
        client.saveChatList(chatList);
    }

    public void removeInvite(ChatInfo chat) throws Exception {
        chatInvites.remove(chat);
    }

    public void inviteChatMember(ChatInfo chat, UserInfo user) throws Exception {
        client.inviteChatMember(chat, user);
    }

    public UserInfo getUser(String username) throws Exception {
        for (UserInfo user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return client.getUserInfo(username);
    }

    public void addContact(UserInfo user) throws Exception {
        users.add(user);

        ContactList contactList = new ContactList();
        contactList.setContacts(Lists.newArrayList(users.iterator()));
        client.saveContactList(contactList);
    }

    public void removeContact(UserInfo user) throws Exception {
        users.remove(user);

        ContactList contactList = new ContactList();
        contactList.setContacts(Lists.newArrayList(users.iterator()));
        client.saveContactList(contactList);
    }

    public String getOnlineStatus(String username) throws IOException, ClassNotFoundException {
        return client.getOnlineStatus(username);
    }

    public ObservableList<UserInfo> getUsers() {
        return users;
    }

    public ObservableList<ChatInfo> getChats() {
        return chats;
    }

    public ObservableList<ChatInfo> getChatInvites() throws Exception {
        loadChatInvites();
        return chatInvites;
    }
}
