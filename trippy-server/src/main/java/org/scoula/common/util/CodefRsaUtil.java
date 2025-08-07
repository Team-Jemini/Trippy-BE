package org.scoula.common.util;

import org.scoula.common.exception.model.ServerErrorException;
import static org.scoula.common.exception.enums.ErrorCode.*;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CodefRsaUtil {

    private static final String CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String KEY_ALGORITHM = "RSA";

    public static String runEncryption(String base64PublicKey, String plainText) {
        try {
            String encrypted = encryptRSA(plainText, base64PublicKey);
            return encrypted;
        } catch (Exception e) {
            throw new ServerErrorException(PASSWORD_ENCRYPTION_FAILED);
        }
    }

    public static String encryptRSA(String plainText, String base64PublicKey) throws Exception {
        byte[] bytePublicKey = Base64.getDecoder().decode(base64PublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytePlain = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(bytePlain);
    }
}
