package hash_checker;

import util.Util;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HashChecker {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        hashChecker("10_mil_pw.txt");
        writeHashToFile("10_mil_pw.txt", "pass_SHA1.txt");
    }

    public static void hashChecker(String inputFileName) throws IOException, NoSuchAlgorithmException {
        File input = new File(inputFileName);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);

                //add code here
        String sha1pw = "2e1a480670e31a5d015e28de043136b62e762d29";
        var sha1pwBytes = Util.hexStringToByteArray(sha1pw);

        var sha1 = MessageDigest.getInstance("SHA-1");

        var maybePassword = br.lines().filter(p -> Arrays.equals(sha1.digest(p.getBytes()), sha1pwBytes))
            .findFirst();

        System.out.println(maybePassword.orElse("something went wrong"));

        br.close();

    }

    public static void writeHashToFile(String inputFileName, String outputFileName) throws IOException, NoSuchAlgorithmException {
        File input = new File(inputFileName);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);

        File output = new File(outputFileName);
        if (!output.exists()){
            output.createNewFile();
        }
        //FileOutputStream fos = new FileOutputStream(output);
        //BufferedOutputStream bos = new BufferedOutputStream(fos);

        FileWriter fw = new FileWriter(output);
        BufferedWriter bw = new BufferedWriter(fw);

        var sha1 = MessageDigest.getInstance("SHA-1");

         br.lines().forEach(p -> {
            try {
                bw.write(getHex(sha1.digest(p.getBytes())));
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
         bw.close();
    }


    public static String getHex(byte[] values) {
        StringBuilder sb = new StringBuilder();
        for (byte value : values) {
            sb.append(String.format("%02x", value));
        }

        return sb.toString();
    }
}
