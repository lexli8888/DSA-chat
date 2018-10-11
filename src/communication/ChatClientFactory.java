package communication;

import net.tomp2p.connection.ChannelClientConfiguration;
import net.tomp2p.connection.ChannelServerConfiguration;
import net.tomp2p.connection.RSASignatureFactory;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;

import java.security.KeyPair;
import java.util.Random;

public class ChatClientFactory {
    public static ChatClient CreateNewClient(KeyPair keyPair) throws Exception {
        Random random = new Random();
        int port = 4000 + random.nextInt(2000);

        PeerBuilder builder = new PeerBuilder(keyPair);

        ChannelServerConfiguration serverConfiguration = builder.createDefaultChannelServerConfiguration();
        ChannelClientConfiguration clientConfiguration = builder.createDefaultChannelClientConfiguration();

        serverConfiguration.signatureFactory(new RSASignatureFactory());
        clientConfiguration.signatureFactory(new RSASignatureFactory());

        builder.channelServerConfiguration(serverConfiguration);
        builder.channelClientConfiguration(clientConfiguration);

        builder.ports(port);

        Peer peer = builder.start();
        PeerBuilderDHT peerBuilderDHT = new PeerBuilderDHT(peer);

        return new ChatClient(peerBuilderDHT.start(), keyPair);
    }
}