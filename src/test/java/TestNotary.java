import io.iconator.testonator.Contract;
import io.iconator.testonator.DeployedContract;
import io.iconator.testonator.TestBlockchain;
import org.junit.jupiter.api.Test;
import org.web3j.abi.datatypes.Type;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import static io.iconator.testonator.TestBlockchain.CREDENTIAL_0;
import static io.iconator.testonator.TestBlockchain.CREDENTIAL_1;
import static io.iconator.testonator.TestBlockchain.compile;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNotary {

    @Test
    public void testContact() throws Exception {
        TestBlockchain blockchain = TestBlockchain.run();

        File contractFile = Paths.get(ClassLoader.getSystemResource("Notary.sol").toURI()).toFile();
        Map<String, Contract> contracts = compile(contractFile);
        assertEquals(1, contracts.size());
        for(String name:contracts.keySet()) {
            System.out.println("Available contract names: " + name);
        }


        DeployedContract dc = blockchain.deploy(CREDENTIAL_0, contracts.get("Notary"));

        byte[] tmp = new byte[32];
        tmp[0]=1;
        tmp[31]=2;

        blockchain.call(CREDENTIAL_0, dc, "store", tmp);

        Type t1 = blockchain.callConstant(dc, "verify", CREDENTIAL_0.getAddress(), tmp).get(0);

        //39 is the timestamp (+13)
        //assertEquals("39", t1.getValue().toString());
    }
}
