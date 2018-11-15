import blockchain.Notary;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

public class TestWeb3j {

    @Test
    public void testContract() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/ef88f3df462a40c2a99c3b25f4b5b98c"));

        Credentials credentials =
                WalletUtils.loadCredentials(
                        "123456",
                        "/Users/pascal/Library/Ethereum/testnet/keystore/UTC--2018-11-15T08-31-20.642000000Z--afa45872b6315e644e31b001d2290958e33289f6.json");

//        System.out.println(GAS_PRICE);
//
//        Notary contract = Notary.deploy(
//                web3j, credentials,
//                GAS_PRICE, GAS_LIMIT).send();
//        String contractAddress = contract.getContractAddress();
//
//        System.out.println(contractAddress);

        byte[] tmp = new byte[32];
        tmp[0]=1;
        tmp[31]=2;


        Notary contract = Notary.load(
                "0xe7abc5cec0d72c01188aa5157820ad7f2257eb51",
                web3j, credentials,
                GAS_PRICE, GAS_LIMIT);

        //System.out.println(contract.store(tmp).send());
        System.out.println(contract.verify("0xafa45872b6315e644e31b001d2290958e33289f6", tmp).send());


        //0xe7abc5cec0d72c01188aa5157820ad7f2257eb51

//
//        contract.store();
//        contract.verify("elias", )

    }
}
