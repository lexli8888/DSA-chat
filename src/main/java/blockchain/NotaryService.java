package blockchain;

import communication.SignatureUtil;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

public class NotaryService {
    private static String WEB3J_ADDRESS = "https://ropsten.infura.io/v3/ef88f3df462a40c2a99c3b25f4b5b98c";
    private static String CONTACT_ADDRESS = "0xe7abc5cec0d72c01188aa5157820ad7f2257eb51";

    private final Web3j web3j = Web3j.build(new HttpService(WEB3J_ADDRESS));
    private final Notary contract;
    private final Credentials credentials;

    public NotaryService(String walletPath, String walletPassword) throws IOException, CipherException {
        credentials = WalletUtils.loadCredentials(
                walletPassword,
                walletPath);

        contract = Notary.load(
                CONTACT_ADDRESS,
                web3j, credentials,
                GAS_PRICE, GAS_LIMIT);
    }

    public void store(byte[] checksum) throws Exception {
        this.contract.store(checksum).send();
    }

    public void store(File file) throws Exception {
        this.store(SignatureUtil.getFileSignature(file));
    }

    public BigInteger verify(byte[] checksum, String address) throws Exception {
        return this.contract.verify(address, checksum).send();
    }

    public BigInteger verify(File file, String address) throws Exception {
        return this.verify(SignatureUtil.getFileSignature(file), address);
    }
}
