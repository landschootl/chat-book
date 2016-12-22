package service;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Classe représentant le service de sécurité.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public class SecurityService {
    public static SecurityService instance = null;

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
            new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    public static SecurityService getInstance() {
        if (instance == null) {
            instance = new SecurityService();
        }
        return instance;
    }

    /**
     * Encrypte une valeur avec l'algorithme AES
     * @param valueToEnc
     * @return
     * @throws Exception
     */
    public String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        return new BASE64Encoder().encode(encValue);
    }

    /**
     * Décrypte une valeur avec l'algorithme AES
     * @param encryptedValue
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    /**
     * Génère un développeur
     * @return
     * @throws Exception
     */
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }
}
