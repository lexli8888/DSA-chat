import communication.SignatureUtil;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestSignatureUtil {

    @Test
    public void testFileSignature() throws Exception {
        String AppDataPath = System.getProperty("user.home") + File.separator + "DSA-Chat";
        String UserSettingPath = AppDataPath + File.separator + "test.txt";
        Path path = Paths.get(UserSettingPath);

        String text = "To be or not to be?";
        Files.write(path, text.getBytes());

        byte[] hash = SignatureUtil.getFileSignature(new File(UserSettingPath));
        assertTrue(hash.length == 32);

    }

}
