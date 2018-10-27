package communication;

import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

public class BootstrapServer {
    public static void ListenOnLocalhost(int port) throws IOException {
        Random rnd = new SecureRandom();
        PeerBuilder builder = new PeerBuilder(Number160.createHash(rnd.nextLong()));
        builder.ports(port);

        System.out.println("starting server on " + port);

        Peer peer = builder.start();
        PeerBuilderDHT peerBuilderDHT = new PeerBuilderDHT(peer);
        peerBuilderDHT.start();
    }


    public static void main(String[] args) throws Exception {

        BootstrapServer.ListenOnLocalhost(4000);
    }
}
