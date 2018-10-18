package communication;

import net.tomp2p.connection.ChannelClientConfiguration;
import net.tomp2p.connection.ChannelServerConfiguration;
import net.tomp2p.connection.RSASignatureFactory;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

public class PeerDHTFactory {
    public static PeerDHT CreatePeer() throws IOException {
        Random random = new SecureRandom();
        int port = 4000 + random.nextInt(2000);

        Number160 peerId = Number160.createHash(random.nextLong());
        PeerBuilder builder = new PeerBuilder(peerId);

        ChannelServerConfiguration serverConfiguration = builder.createDefaultChannelServerConfiguration();
        ChannelClientConfiguration clientConfiguration = builder.createDefaultChannelClientConfiguration();

        serverConfiguration.signatureFactory(new RSASignatureFactory());
        clientConfiguration.signatureFactory(new RSASignatureFactory());

        builder.channelServerConfiguration(serverConfiguration);
        builder.channelClientConfiguration(clientConfiguration);

        builder.ports(port);

        Peer peer = builder.start();
        PeerBuilderDHT peerBuilderDHT = new PeerBuilderDHT(peer);

        return peerBuilderDHT.start();
    }
}