package communciation.test;

import communication.ChatClient;
import communication.UserInfo;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.nat.PeerNAT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestBootstrapServer {

    @Test
    public void testRemoteBootstrap() throws Exception {

        ChatClient client = new ChatClient();

//
//        Peer peer = new PeerMaker(new Number160(rnd)).ports(4000).setBehindFirewall().makeAndListen();
//        PeerNAT peerNAT = new PeerNAT(peer);
//        PeerAddress pa = new PeerAddress(Number160.ZERO, InetAddress.getByName("apps.bertschi.io"), 4000, 4000);
////Check if peer is reachable from the internet
//        FutureDiscover fd = peer.discover().peerAddress(pa).start();

        client.discoverOnInet("apps.bertschi.io", 4000);
        UserInfo info = client.getUserInfo("pascal");

        assertNotNull(info);
        assertEquals("Pascal", info.getFirstName());
        assertEquals("Bertschi", info.getLastName());
    }
}
