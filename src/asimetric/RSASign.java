package asimetric;

import java.io.*;
import java.security.*;

public class RSASign {
    public static byte[] generateDigitalSignature(String filename, PrivateKey privKey, String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        File file = new File(filename);
        if (!file.exists())
            file.createNewFile();

        FileInputStream fis = new FileInputStream(file);

        byte[] fileContent = fis.readAllBytes();

        fis.close();

        Signature digitalSignature = Signature.getInstance(algorithm);
        digitalSignature.initSign(privKey);
        digitalSignature.update(fileContent); //pas obligatoriu

        return digitalSignature.sign();
    }

    public static void validateDigitalSIgnature(String fileName, PublicKey pubKey, byte[] digSign, String signAlgorithm ) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(file);

        byte[] fileContent = fis.readAllBytes();
        fis.close();

        Signature verify = Signature.getInstance(signAlgorithm);
        verify.initVerify(pubKey);
        verify.update(fileContent);

        System.out.println("Signature is valid:  " + verify.verify(digSign));
    }
}
