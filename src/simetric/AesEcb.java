package simetric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class AesEcb {
    //metoda criptare
    public static void encrypt(
        String inputFile,
        String outputFile,
        byte[]  key,
        String provider)
        throws NoSuchAlgorithmException,
        NoSuchProviderException,
        NoSuchPaddingException,
        InvalidKeyException,
        IOException,
        ShortBufferException,
        IllegalBlockSizeException,
        BadPaddingException{

        //deschidem fisierele
        File fisierIntrare = new File(inputFile);
        if(!fisierIntrare.exists())
            throw new FileNotFoundException();

        FileInputStream fis = new FileInputStream(fisierIntrare);
        FileOutputStream fos = new FileOutputStream(outputFile);

        //1. initializare cifru - algoritm
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", provider);

        //2. Definire cheie + initializare cifru
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        //3. Citire blocuri + criptare
        byte[] buffer = new byte[cipher.getBlockSize()];
        int noReadBytes = 0;
        byte[] encryptedBuffer = null;

        while((noReadBytes = fis.read(buffer))!=-1){
            encryptedBuffer =
                new byte[cipher.getOutputSize(
                    noReadBytes)];
            int noBytes =
                cipher.update(
                    buffer, 0, noReadBytes, encryptedBuffer,0);
            fos.write(encryptedBuffer, 0, noBytes);
        }

        //4. Obtinem ultimul bloc criptat
        int noBytes = cipher.doFinal(
            encryptedBuffer, 0);
        fos.write(encryptedBuffer,0,noBytes);

        //5. inchidem fisierele
        fis.close();
        fos.close();

    }

    //metoda decriptare
    public static void decrypt(
        String inputFile,
        String outputFile,
        byte[]  key,
        String provider)
        throws NoSuchAlgorithmException,
        NoSuchProviderException,
        NoSuchPaddingException,
        InvalidKeyException,
        IOException,
        ShortBufferException,
        IllegalBlockSizeException,
        BadPaddingException{

        //deschidem fisierele
        File fisierIntrare =
            new File(inputFile);
        if(!fisierIntrare.exists())
            throw new FileNotFoundException();

        FileInputStream fis =
            new FileInputStream(fisierIntrare);
        FileOutputStream fos =
            new FileOutputStream(outputFile);

        //1. initializare cifru - algoritm
        Cipher cipher =
            Cipher.getInstance(
                "AES/ECB/PKCS5Padding", provider);
        //2. Definire cheie + initializare cifru
        SecretKey secretKey =
            new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        //3. Citire blocuri + criptare
        byte[] buffer = new byte[cipher.getBlockSize()];
        int noReadBytes = 0;
        byte[] encryptedBuffer = null;

        while((noReadBytes = fis.read(buffer))!=-1){
            encryptedBuffer =
                new byte[cipher.getOutputSize(
                    noReadBytes)];
            int noBytes =
                cipher.update(
                    buffer, 0, noReadBytes, encryptedBuffer,0);
            fos.write(encryptedBuffer, 0, noBytes);
        }

        //4. Obtinem ultimul bloc criptat
        int noBytes = cipher.doFinal(
            encryptedBuffer, 0);
        fos.write(encryptedBuffer,0,noBytes);

        //5. inchidem fisierele
        fis.close();
        fos.close();

    }
}
