package com.ceva.base.common.utils;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class TripleDESEncryption {
	private static final String CHARSET_NAME = "UTF-8";
	protected static final String SEED_KEY = "pame!@#$9308ghjk%^&*P";
	private static final String DIGEST_ALGORITHM = "md5";
	private static final String CIPHER_TRANSFORMATION = "DESede/CBC/PKCS5Padding";

	public static enum Alogorithm {
		DESede, TripleDES;

		private Alogorithm() {
		}
	}

	protected String encryptAndReturnText(String message, String seedKey)
			throws Exception {
		return new String(Base64.encodeBase64(encryptAndReturnBytes(message,
				seedKey)));
	}

	protected byte[] encryptAndReturnBytes(String message, String seedKey)
			throws Exception {
		byte[] plainTextBytes = message.getBytes("UTF-8");
		byte[] cipherText = getCipher(1, seedKey).doFinal(plainTextBytes);
		return cipherText;
	}

	protected String decrypt(byte[] message, String seedKey) throws Exception {
		byte[] plainText = getCipher(2, seedKey).doFinal(message);
		return new String(plainText, "UTF-8");
	}

	protected String decrypt(String message, String seedKey) throws Exception {
		return decrypt(Base64.decodeBase64(message), seedKey);
	}

	private Cipher getCipher(int cipherMode, String seedKey) throws Exception {
		SecretKey key = new SecretKeySpec(getSalt(seedKey),
				Alogorithm.DESede.name());
		IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(cipherMode, key, iv);
		return cipher;
	}

	private byte[] getSalt(String seedKey) throws Exception {
		MessageDigest md = MessageDigest.getInstance("md5");
		byte[] digestOfPassword = md.digest(seedKey.getBytes("UTF-8"));
		byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
		int j = 0;
		for (int k = 16; j < 8;) {
			keyBytes[(k++)] = keyBytes[(j++)];
		}
		return keyBytes;
	}

	public static void main(String[] args) throws Exception {
		new TripleDESEncryption().decrypt("z2se05iRf8PFDzTw9Bdkbhfdqlw=", SEED_KEY);
	}
}