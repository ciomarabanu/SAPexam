package asimetric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class KeyStoreManager {

    //    =====   LOAD KEYSTORE   =====

    public static KeyStore loadKeyStore(String ksFileName,
                                     String ksPass,
                                     String ksType) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {

        File file = new File(ksFileName);
        if(!file.exists()) {
            System.out.println("NO SUCH FILE!!!");
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(file);

        KeyStore ks = KeyStore.getInstance(ksType);
        ks.load(fis, ksPass.toCharArray());

        fis.close();
        return ks;
        //toate datele au fost incarcate in memorie.
        // avem un ks virtual
    }

    //  ====== ITERATE THROUGH KeyStore  =======

    public static void listKeyStoreContent(KeyStore ks) throws KeyStoreException {
        System.out.println("Key store content: ");
        Enumeration<String> aliases = ks.aliases();

        while(aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            System.out.println("Key store entry: " + alias);
            if(ks.isCertificateEntry(alias)) {
                System.out.println("Is public key certificate");
            }  //daca e key publica; .isKeyEntry == key privata
            if(ks.isKeyEntry(alias)) {
                System.out.println("Is private - public pair");
            }
        }

    }

    // ===== EXTRACT PUBLIC KEY FROM .cer ======

    public static PublicKey getCertificateKey(String x509certificateFile) throws IOException, CertificateException {
        File file = new File(x509certificateFile);

        if(!file.exists()) {
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(file);

        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) cerFactory.generateCertificate(fis);
        fis.close();

        return certificate.getPublicKey();
    }


    // ========== get PUBLIC key from key store ==========

    public static PublicKey getKeyStorePublicKey(KeyStore keyStore, String alias) throws KeyStoreException {
        if(keyStore == null) {
            System.out.println("no such keystore");
            throw new UnsupportedOperationException();
        }
        if(keyStore.containsAlias(alias)) {
            return keyStore.getCertificate(alias).getPublicKey();
        }else {
            System.out.println("no such alias");
            throw new UnsupportedOperationException();
        }
    }


    // ======== get PRIVATE key from key store=========

    public static PrivateKey getKeyStorePrivatekey(KeyStore keyStore, String alias, String keyPass)
        throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {

        if(keyStore == null) {
            System.out.println("no such keystore");
            throw new UnsupportedOperationException();
        }
        if(keyStore.containsAlias(alias)) {
            return (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());
        }else {
            System.out.println("no such alias");
            throw new UnsupportedOperationException();
        }
    }

}
