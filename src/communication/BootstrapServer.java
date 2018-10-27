package communication;

import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import static communication.PeerDHTFactory.CreatePeer;

public class BootstrapServer {
    public static void ListenOnLocalhost(int port) throws IOException {
       PeerDHT peer = CreatePeer(port);
    }


    public static void main(String[] args) throws Exception {

        BootstrapServer.ListenOnLocalhost(4000);
    }
}
