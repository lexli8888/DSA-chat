package communciation.test;

import communication.ChatClient;
import communication.UserInfo;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;

public class UserInfoFactory {
    public static List<UserInfo> getUsers(int amount) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        List<UserInfo> users = new ArrayList<>();
        for(int i = 0; i<amount;i++) {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = generator.generateKeyPair();

            UserInfo user = UserInfo.New(keyPair.getPublic(), "user" + i, "test" + i, "test" + i);
            users.add(user);
        }

        return users;
    }
}
