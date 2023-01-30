package simetric;

import util.Util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AES_ECB_enc {
    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
       // encryptECB("Accounts.txt", "User9.key", "AES_ECB_output.txt", "AES");
        decryptECB("AES_ECB_output.txt", "User9.key", "decrypted.txt", "AES");
    }

    public static void encryptECB(String inputFileName,
                                  String keyFileName,
                                  String outputFileName,
                                  String algorithm)
        throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        //==== open input file ====
        File input = new File(inputFileName);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);

        //==== open key file ====
        File keyFile = new File(keyFileName);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(keyFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        var key = bis.readAllBytes();

        //==== open output file ====
        File output = new File(outputFileName);
        if (!output.exists()) {
            output.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(output);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        
        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        var b = cipher.doFinal(key);
        var newLine = System.getProperty("line.separator").getBytes();

        var encriptedHashes = br.lines()
             .flatMap(line -> Stream.of(line.split(" = ")[1]))   //retrieve hash
             .map(hash -> Util.hexStringToByteArray(hash))            //convert to bytes
             .map(bytes -> {
                 try {
                     return cipher.doFinal(bytes);                  //encrypt
                 } catch (IllegalBlockSizeException e) {
                     e.printStackTrace();
                 } catch (BadPaddingException e) {
                     e.printStackTrace();
                 }
                 return new byte[0];
             })
            .map(enc -> Util.getHex(enc))                   //convert to hex string
            .collect(Collectors.toList());

         for (var encHash : encriptedHashes) {        //write to file
             bos.write(encHash.getBytes());
             bos.write(newLine);
         }

        br.close();
        bos.flush();
        bos.close();
    }


    public static void decryptECB(String inputFileName,
                                  String keyFileName,
                                  String outputFileName,
                                  String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        File input = new File(inputFileName);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);

        File keyFile = new File(keyFileName);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(keyFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        var key = bis.readAllBytes();


        File output = new File(outputFileName);
        if (!output.exists()) {
            output.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(output);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, algorithm);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        var decrypted = br.lines()
            .map(enc -> Util.hexStringToByteArray(enc))
            .map(enc -> {
                try {
                   return cipher.doFinal(enc);
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
                return new byte[0];
            })
            .map(enc -> Util.getHex(enc))
            .collect(Collectors.toList());

        var newLine = System.getProperty("line.separator").getBytes();
        for (var line : decrypted) {
            bos.write(line.getBytes());
            bos.write(newLine);
        }

        br.close();
        bos.close();

    }
}
