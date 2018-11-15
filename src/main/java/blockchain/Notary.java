package blockchain;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.6.0.
 */
public class Notary extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061014f806100206000396000f3fe60806040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416630dd5ee1b8114610050578063654cf88c146100a8575b600080fd5b34801561005c57600080fd5b506100966004803603604081101561007357600080fd5b5073ffffffffffffffffffffffffffffffffffffffff81351690602001356100d4565b60408051918252519081900360200190f35b3480156100b457600080fd5b506100d2600480360360208110156100cb57600080fd5b5035610107565b005b73ffffffffffffffffffffffffffffffffffffffff91909116600090815260208181526040808320938352929052205490565b336000908152602081815260408083209383529290522042905556fea165627a7a7230582074cb265f4e1b080602c3a81ef4285dd9f60bb2a63df20e8043a59ca342b0f5820029";

    public static final String FUNC_VERIFY = "verify";

    public static final String FUNC_STORE = "store";

    @Deprecated
    protected Notary(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    protected Notary(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Notary(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> verify(String recipient, byte[] hash) {
        final Function function = new Function(FUNC_VERIFY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(recipient),
                        new org.web3j.abi.datatypes.generated.Bytes32(hash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> store(byte[] hash) {
        final Function function = new Function(
                FUNC_STORE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static RemoteCall<Notary> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Notary.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Notary> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Notary.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static Notary load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Notary(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Notary load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Notary(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Notary load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Notary(contractAddress, web3j, transactionManager, contractGasProvider);
    }
}
