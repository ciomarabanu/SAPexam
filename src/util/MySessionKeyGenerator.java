package util;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;

public class MySessionKeyGenerator {
    public static byte[] getRandomSessionKey(String algorithm, int noBytes) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(noBytes);
        return keyGenerator.generateKey().getEncoded();
    }
}
