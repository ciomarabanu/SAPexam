package rainbow_table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RainbowTable {
    public static void rainbowTable(String fileName) throws FileNotFoundException, NoSuchAlgorithmException {
        long tstart = System.currentTimeMillis();

        File passwords = new File(fileName);
        if (!passwords.exists()) {
            throw new FileNotFoundException();
        }

        FileReader fr = new FileReader(passwords);
        BufferedReader br = new BufferedReader(fr);

        String toBeCompared = "81272d199f167e7ba4ec2a27d33ab72195791643820409d993e6d7b93d28498e";
        var md5 = MessageDigest.getInstance("MD5");
        var sha256 = MessageDigest.getInstance("SHA-256");

        var pw1 = "ismsap" + br.lines().findFirst().get();
        System.out.println(pw1);
        byte[] md5Hash = md5.digest(pw1.getBytes());
        System.out.println(getHex(md5Hash));
        byte[] sha256Hash = sha256.digest(md5Hash);
        System.out.println(getHex(sha256Hash));

        var maybePassword = br.lines()
            .filter(pw -> getHex(sha256.digest(md5.digest(("ismsap" + pw).getBytes()))).equals(toBeCompared))
            .findFirst();


        System.out.println(maybePassword.orElse("no password found"));

        long tfinal = System.currentTimeMillis();
        System.out.println("Duration is : " + (tfinal-tstart));
    }

    public static String getHex(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (byte val : input) {
            result.append(String.format("%02x", val));
        }
        return result.toString();
    }
}
