package asimetric;

import util.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;

public class TestRSA {

    public static void main(String[] args) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        KeyStore keyStore = KeyStoreManager.loadKeyStore("ismkeystore.ks", "passks", "pkcs12");
        KeyStoreManager.listKeyStoreContent(keyStore);

        PublicKey ism1PubKey = KeyStoreManager.getCertificateKey("ISMCertificateX509.cer");
        System.out.println("ism1 key algorithm is " + ism1PubKey.getAlgorithm());
        System.out.println(Util.getHex(ism1PubKey.getEncoded()));


        PublicKey ism1PubKeyFromKS = KeyStoreManager.getKeyStorePublicKey(keyStore, "ismkey1");
        PrivateKey ism1PrivKeyFromKS = KeyStoreManager.getKeyStorePrivatekey(keyStore, "ismkey1", "passks");

        System.out.println("public key " + Util.getHex(ism1PubKeyFromKS.getEncoded()));
        System.out.println("private key " + Util.getHex(ism1PrivKeyFromKS.getEncoded()));

        if (Arrays.equals(ism1PubKeyFromKS.getEncoded(), ism1PubKey.getEncoded())) {
            System.out.println("Identical keys");
        }

        byte[] AESRandomSessionKey = Util.getRandomBytes(16, null);
        System.out.println("AES session key: " + Util.getHex(AESRandomSessionKey));

        byte[] encryptedSessionKey = RSACipher.encrypt(ism1PrivKeyFromKS, AESRandomSessionKey);
        byte[] decryptedSessionKey = RSACipher.decrypt(ism1PubKey, encryptedSessionKey);

        if (Arrays.equals(AESRandomSessionKey, decryptedSessionKey)) {
            System.out.println("session keys are identical");
        } else {
            System.out.println("huston we have different keys");
        }
    }

}