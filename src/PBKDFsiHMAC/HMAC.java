package PBKDFsiHMAC;

//documentatie java MAC eg. creating a MAC object

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/* HMAC ALGORITHMS:
Algorithm Name	Description
HmacMD5	------>  The HMAC-MD5 keyed-hashing algorithm as defined in RFC 2104
                 "HMAC: Keyed-Hashing for Message Authentication" (February 1997).

HmacSHA1       -------->
HmacSHA224     -------->
HmacSHA256     -------->   The HmacSHA* algorithms as defined in RFC 2104 "HMAC:
HmacSHA384     -------->   Keyed-Hashing for Message Authentication" (February 1997)
HmacSHA512     -------->   with SHA-* as the message digest algorithm.

PBEWith<mac>   -------->  	Mac for use with the PKCS #5 password-based message authentication standard,
                            where <mac> is a Message Authentication Code algorithm name.
                            Example: PBEWithHmacSHA1.

KEY:
You can initialize your Mac object with any (secret-)key object that implements
the javax.crypto.SecretKey interface. This could be an object returned by
javax.crypto.KeyGenerator.generateKey(), or one that is the result of a key agreement protocol,
as returned by javax.crypto.KeyAgreement.generateSecret(), or an instance of javax.crypto.spec.SecretKeySpec.

Key ALGORITHM :
HmacMD5	Key generator for use with the HmacMD5 algorithm.
HmacSHA1
HmacSHA224
HmacSHA256
HmacSHA384
HmacSHA512	Keys generator for use with the various flavors of the HmacSHA algorithms.
* */

public class HMAC {
    //same algorithm for both HMAC and HmacKey
    public static byte[] getHMAC (byte[] input, String key, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {

        Mac hmac = Mac.getInstance(algorithm);
        Key hmacKey = new SecretKeySpec(key.getBytes(), algorithm);
        hmac.init(hmacKey);


        //one step hmac
        return hmac.doFinal(input); //doFinal in loc de .digest de la MessageDigest
    }

    //implementare pt un fisier

    public static byte[] getHMAC (String fileName, String key, String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        //deschidem fisierul
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);

        byte[] buffer = new byte[10];
        int noBytes = 0;

        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key.getBytes(), algorithm));

        while (noBytes != -1) {
            noBytes = bis.read(buffer);
            if(noBytes != -1)
            mac.update(buffer, 0, noBytes); //proceseaza de fiecare data cat a citit
        }
        bis.close();

        return mac.doFinal();
    }



}
