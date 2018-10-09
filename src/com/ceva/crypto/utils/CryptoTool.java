package com.ceva.crypto.utils;

import com.ceva.crypto.exception.CryptoException;
import com.ceva.crypto.utils.CryptoTool;
import com.ceva.crypto.utils.StringEncrypter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;

import javax.crypto.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CryptoTool {
	private static final Log LOG = LogFactory.getLog(CryptoTool.class);
	private static StringEncrypter cryptoTool;
	private static SecretKey secKey;
	private static String keystorePath;
	private static String keyAlias;
	private static String keyPassword;
	private static String keystorePassword;
	private static String algorithm;

	public static void cryptoTool() {
		if ((keystorePath == null) || (keystorePassword == null)
				|| (keyAlias == null) || (keyPassword == null)
				|| (algorithm == null)) {
			LOG.error("Either of the input values are null. Please check the configurations.");
			cryptoTool = null;
			secKey = null;
		} else {
			try {
				secKey = getKeyFromJKS(keystorePath, keystorePassword,
						keyAlias, keyPassword);

				if (secKey == null)
					throw new Exception("couldnot retreive key from keystore");
				cryptoTool = new StringEncrypter(secKey, algorithm);
				if (cryptoTool == null)
					throw new Exception("error while getting key from keystore");
			} catch (Exception e) {
				LOG.error("Exception while initializing the Encrypter " + e);
				cryptoTool = null;
				secKey = null;
			}
		}
	}

	private static SecretKey getKeyFromJKS(String keystorePath,
			String keystorePassword, String keyAlias, String keyPassword)
			throws Exception {
		FileInputStream fis = null;
		try {
			KeyStore keystore = KeyStore.getInstance("JCEKS");
			fis = new FileInputStream(keystorePath);
			keystore.load(fis, keystorePassword.toCharArray());

			SecretKey myKey = (SecretKey) keystore.getKey(keyAlias,
					keyPassword.toCharArray());

			SecretKey localSecretKey1 = myKey;
			return localSecretKey1;
		} catch (Exception e) {
			LOG.error("Exception while getting the key from keystore " + e);
			throw new Exception("[getKeyFromJKS] Exception ..", e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
			}
		}

	}

	public static String encrypt(String clearText) throws InvalidKeyException,
			CryptoException {
		if (cryptoTool == null)
			throw new InvalidKeyException(
					"The encrypter object couldnot be instantiated. Check logs for details");
		try {
			return cryptoTool.encrypt(clearText);
		} catch (BadPaddingException e) {
			throw new CryptoException("exception while encrypting data", e);
		} catch (IllegalBlockSizeException e) {
			throw new CryptoException("exception while encrypting data illegal", e);
		} catch (UnsupportedEncodingException e) {
			throw new CryptoException("exception while encrypting data unsupported", e);
		} catch (Exception e) {
		}
		throw new CryptoException("Exception while encrypting data.");
	}

	public static String decrypt(String cipherText) throws InvalidKeyException,
			CryptoException {
		if (cryptoTool == null)
			throw new InvalidKeyException(
					"The encrypter object couldnot be instantiated. Check logs for details");
		try {
			return cryptoTool.decrypt(cipherText);
		} catch (BadPaddingException e) {
			throw new CryptoException("exception while decrypting data", e);
		} catch (IllegalBlockSizeException e) {
			throw new CryptoException("exception while decrypting data illegal", e);
		} catch (UnsupportedEncodingException e) {
			throw new CryptoException("exception while decrypting data crypto", e);
		} catch (IOException e) {
			throw new CryptoException("exception while decrypting data IOException", e);
		} catch (Exception e) {
		}
		throw new CryptoException("Exception while decrypting data");
	}

	public static byte[] HexStringToByteArray(String strHex) {
		byte[] bytKey = new byte[strHex.length() / 2];
		int y = 0;

		for (int x = 0; x < bytKey.length; x++) {
			String strbyte = strHex.substring(y, y + 2);
			if (strbyte.equals("FF"))
				bytKey[x] = -1;
			else {
				bytKey[x] = (byte) Integer.parseInt(strbyte, 16);
			}
			y += 2;
		}
		return bytKey;
	}

	public static String ByteArrayToHexString(byte[] byteArray) {
		StringBuffer strArray = new StringBuffer();
		strArray.append("");
		for (int x = 0; x < byteArray.length; x++) {
			int b = byteArray[x] & 0xFF;
			if (b < 16) {
				strArray.append("0");
				strArray.append(Integer.toHexString(b).toUpperCase());
			} else {
				strArray.append(Integer.toHexString(b).toUpperCase());
			}
		}
		return strArray.toString();
	}

	public static void setKeystorePath(String keystorePath1) {
		keystorePath = keystorePath1;
	}

	public static void setKeyAlias(String keyAlias1) {
		keyAlias = keyAlias1;
	}

	public static void setKeyPassword(String keyPassword1) {
		keyPassword = keyPassword1;
	}

	public static void setKeystorePassword(String keystorePassword1) {
		keystorePassword = keystorePassword1;
	}

	public static void setAlgorithm(String algorithm1) {
		algorithm = algorithm1;
	}
}