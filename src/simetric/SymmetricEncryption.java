package simetric;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SymmetricEncryption {

    //ECB = electronic code book
    //algorithm - DES, tripleDES, AES
    //cipher class in java documentation
    //cipher trebuie initializat cu key

    public static void encryptECB (String inputFile, String key, String algorithm, String cypherfile) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        File input = new File(inputFile);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(input);
        BufferedInputStream bis = new BufferedInputStream(fis); //pregatit pt citire

        File output = new File(cypherfile);
        if (!output.exists()) {
            output.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(output);
        BufferedOutputStream bos = new BufferedOutputStream(fos); //pregatit pt scriere

        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noBytes = 0;
        byte[] cipherBuffer;

        while(noBytes != -1) {
            noBytes = bis.read(buffer); //la ultimul bloc trbeuie sa apelezi "doFinal"
                                         // ca sa aplice bitii de padding daca sunt necesari.
                                        // altfel n-o sa citeasca nimic
            if (noBytes != -1) {
                cipherBuffer = cipher.update(buffer, 0, noBytes);
                bos.write(cipherBuffer);
            }

        }
        cipherBuffer = cipher.doFinal();
        bos.write(cipherBuffer);

        bis.close();
        bos.close();

    }

    public static void decryptECB (String cipherFile, String key, String algorithm, String plainFile) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        File input = new File(cipherFile);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(input);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File output = new File(plainFile);
        if (!output.exists()) {
            output.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(output);
        BufferedOutputStream bos = new BufferedOutputStream(fos); //am deschis fisierele

        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), algorithm));

        byte[] inputBuffer = new byte[cipher.getBlockSize()];
        byte[] outputBuffer;
        var noBytes = 0;

        while (noBytes != -1) {
           noBytes = bis.read(inputBuffer); //citeste in inputBuffer SI intoarce nr de bytes citit
           if (noBytes != -1) {
               outputBuffer = cipher.update(inputBuffer, 0, noBytes);
               bos.write(outputBuffer); //citeste in outputBuffer
           }
        }
        outputBuffer = cipher.doFinal();
        bos.write(outputBuffer);

        bos.close();
        bis.close();
    }


    /** la fel ca ECB dar are nevoie de un IV (initialization vector)
     * pe care am decis sa il "stocam" in fisierul encriptat (cipher file)**/
    public static void encryptCBC (String inputFile, String key, String algorithm, String cypherfile) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        File input = new File(inputFile);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(input);
        BufferedInputStream bis = new BufferedInputStream(fis); //pregatit pt citire

        File output = new File(cypherfile);
        if (!output.exists()) {
            output.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(output);
        BufferedOutputStream bos = new BufferedOutputStream(fos); //pregatit pt scriere

        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);

        //generate new random IV for each encryption.
        // de feicare data cand generam programul, fisierul va fi criptat diferit
        // pt ca se schimba IV-ul
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = SecureRandom.getSeed(cipher.getBlockSize());
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        //write IV in the cipher file
        bos.write(iv);
        //acum am iv-ul in text la inceput; va fi folosit pt criptare.
        //nu face parte din secret

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noBytes = 0;
        byte[] cipherBuffer;

        while(noBytes != -1) {
            noBytes = bis.read(buffer); //la ultimul bloc trbeuie sa apelezi "doFinal"
            // ca sa aplice bitii de padding daca sunt necesari.
            // altfel n-o sa citeasca nimic
            if (noBytes != -1) {
                cipherBuffer = cipher.update(buffer, 0, noBytes);
                bos.write(cipherBuffer);
            }

        }
        cipherBuffer = cipher.doFinal();
        bos.write(cipherBuffer);

        bis.close();
        bos.close();

    }



    /** la decriptare nu mai genereg IV-ul,
     *  pt ca il am deja in prima parte a fisierului encriptat**/
    public static void decryptCBC (String inputCipher, String key, String algorithm, String plaintextfile) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        File input = new File(inputCipher);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(input);
        BufferedInputStream bis = new BufferedInputStream(fis); //pregatit pt citire

        File output = new File(plaintextfile);
        if (!output.exists()) {
            output.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(output);
        BufferedOutputStream bos = new BufferedOutputStream(fos); //pregatit pt scriere

        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);

        //get IV from cipherfile (first block)
        byte[] iv = new byte[cipher.getBlockSize()];
        bis.read(iv);

        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noBytes = 0;
        byte[] cipherBuffer;

        while(noBytes != -1) {
            noBytes = bis.read(buffer); //la ultimul bloc trbeuie sa apelezi "doFinal"
            // ca sa aplice bitii de padding daca sunt necesari.
            // altfel n-o sa citeasca nimic
            if (noBytes != -1) {
                cipherBuffer = cipher.update(buffer, 0, noBytes);
                bos.write(cipherBuffer);
            }

        }
        cipherBuffer = cipher.doFinal();
        bos.write(cipherBuffer);

        bis.close();
        bos.close();

    }


}
