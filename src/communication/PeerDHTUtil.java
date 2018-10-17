package communication;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PeerDHTUtil {


    public static boolean putData(PeerDHT peer, KeyPair protection, String locationKey, String contentKey, String data, Integer ttl) throws IOException {
        Data putData = new Data(data);

        if (ttl != null) {
            putData.ttlSeconds(ttl);
        }

        putData.protectEntry(protection);

        Number160 locationKeyNo = Number160.createHash(locationKey);
        Number160 contentKeyNo = Number160.createHash(contentKey);

        FuturePut put = peer.put(locationKeyNo)
                .data(contentKeyNo, putData)
                .sign()
                .start();

        put.awaitUninterruptibly();
        put.futureRequests().awaitUninterruptibly();

        return put.isSuccess();
    }

    public static boolean putEncryptedData(PeerDHT peer, KeyPair protection, PublicKey encryptionKey, String locationKey, String contentKey, String data, Integer ttl) throws Exception {
        String encryptedData = EncryptionUtil.encryptData(encryptionKey, data);
        return putData(peer, protection, locationKey, contentKey, encryptedData, ttl);
    }

    public static String getEncryptedData(PeerDHT peer, KeyPair keyPair, String locationKey, String contentKey) throws Exception {
        String data = getData(peer, locationKey, contentKey);
        if (data == null) {
            return null;
        }

        return EncryptionUtil.decryptData(keyPair.getPrivate(), data);
    }

    public static List<String> getEncryptedData(PeerDHT peer, KeyPair keyPair, String locationKey) throws Exception {
        List<String> data = getData(peer, locationKey);
        List<String> result = new ArrayList<>();
        for(String item : data) {
            result.add(EncryptionUtil.decryptData(keyPair.getPrivate(), item));
        }
        return result;
    }


    public static List<String> getData(PeerDHT peer, String locationKey) throws IOException, ClassNotFoundException {
        Number160 locationKeyNo = Number160.createHash(locationKey);

        FutureGet futureGet = peer.get(locationKeyNo)
                .start();
        futureGet.awaitUninterruptibly();

        if (!futureGet.isSuccess()) {
            return null;
        }

        Collection<Data> values = futureGet.dataMap().values();
        List<String> result = new ArrayList<>();

        for (Data value : values) {
            result.add(value.object().toString());
        }

        return result;
    }

    public static String getData(PeerDHT peer, String locationKey, String contentKey) throws IOException, ClassNotFoundException {
        Number160 locationKeyNo = Number160.createHash(locationKey);
        Number160 contentKeyNo = Number160.createHash(contentKey);

        FutureGet futureGet = peer.get(locationKeyNo)
                .contentKey(contentKeyNo)
                .start();
        futureGet.awaitUninterruptibly();

        if (!futureGet.isSuccess()) {
            return null;
        }

        Iterator<Data> values = futureGet.dataMap().values().iterator();
        if (values.hasNext()) {
            Data data = values.next();
            return data.object().toString();
        } else {
            return null;
        }
    }
}
