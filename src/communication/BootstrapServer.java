package communication;

import net.tomp2p.dht.PeerDHT;

import java.io.IOException;

import static communication.PeerDHTFactory.CreatePeer;

public class BootstrapServer {
    public static void ListenOnLocalhost(int port) throws IOException {
       PeerDHT peer = CreatePeer(port);
    }


    public static void main(String[] args) throws Exception {

        BootstrapServer.ListenOnLocalhost(4000);
    }
}
