package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Util {

    public static String getHex(byte[] values) {
        StringBuilder sb = new StringBuilder();
        for (byte value : values) {
            sb.append(String.format("%02x", value));
        }

        return sb.toString();
    }


    public static byte[] hexStringToByteArray(String s) {
        if (s.length() % 2 == 1) {
            System.out.println("invalid hex string length");
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    public static byte[] getSHA1(byte[] values) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return md.digest(values);
    }


    public static byte[] getSHA256(byte[] values) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        //md.update(values);
        return md.digest(values);
    }


    public static byte[] getRandomBytes(int noBytes, byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        if(seed != null) {
            secureRandom.setSeed(seed);
        }
        byte[] randomBytes = new byte[noBytes];
        secureRandom.nextBytes(randomBytes);

        return randomBytes;
    }
}
