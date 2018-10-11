package communication;

import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

import java.io.IOException;

public class BootstrapServer {
    public static void ListenOnLocalhost(int port) throws IOException {
        PeerBuilder builder = new PeerBuilder(Number160.createHash(1));
        builder.ports(port);

        Peer peer = builder.start();
        PeerBuilderDHT peerBuilderDHT = new PeerBuilderDHT(peer);
        peerBuilderDHT.start();
    }


    public static void main(String[] args) throws Exception {
        BootstrapServer.ListenOnLocalhost(4000);
    }
}
