package PBKDFsiHMAC;

// nu se aplica pe un fisier!!
// pornim de la un password. functiile astea deriveaza alte valori
// rolul lor e sa genereze alte key
// standard algorithm name etc... java
// JAVA secret key factory class??

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/*
* PBEWith<digest>And<encryption>
PBEWith<prf>And<encryption>	Secret-key factory for use with PKCS5 password-based encryption,
where <digest> is a message digest, <prf> is a pseudo-random function, and <encryption> is an
encryption algorithm.
Examples:

PBEWithMD5AndDES (PKCS #5, 1.5),
PBEWithHmacSHA256AndAES_128 (PKCS #5, 2.0)
Note: These all use only the low order 8 bits of each password character.
*
PBKDF2With<prf>	Password-based key-derivation algorithm found in PKCS #5
                using the specified pseudo-random function (<prf>). Example: PBKDF2WithHmacSHA256.
* */

public class PBKDF {

    public static byte[] getPBKDFvalue
        (String password, byte[] salt, int noIterations, String algorithm, int outputSize)
        throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory pbkdf = (SecretKeyFactory) SecretKeyFactory.getInstance(algorithm);//fol numele din documentatie in paranteza
        PBEKeySpec pbkdfSpecifications = new PBEKeySpec(password.toCharArray(), salt, noIterations, outputSize);
        SecretKey generatedKey = pbkdf.generateSecret(pbkdfSpecifications);

        return generatedKey.getEncoded();
    }

}
