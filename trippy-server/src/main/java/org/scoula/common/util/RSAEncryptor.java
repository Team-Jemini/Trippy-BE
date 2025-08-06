package org.scoula.common.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncryptor {

	public static String encrypt(String plainText, String publicKeyPEM) throws Exception {
		String publicKeyContent = publicKeyPEM
			.replaceAll("\\n", "")
			.replaceAll("-----BEGIN PUBLIC KEY-----", "")
			.replaceAll("-----END PUBLIC KEY-----", "");

		byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(spec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encrypted);
	}
}
