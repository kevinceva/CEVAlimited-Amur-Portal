package com.ceva.base.common.utils;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;

public class EncryptTransactionPin {

	protected static Logger logger = Logger
			.getLogger(EncryptTransactionPin.class);

	public static String encrypt(String key, String password, char pad)
			throws Exception {
		String encryptData = "";
		byte[] byteKey = null;
		byte[] bytePwd = null;
		byte[] enc = null;
		try {
			byteKey = hex2byte(key);
			bytePwd = hex2byte(rightPadding(password, 16, pad));
			enc = CryptoUtil.desede(bytePwd, byteKey, Cipher.ENCRYPT_MODE);
			encryptData = Util.hexString(enc);
		} catch (Exception e) {
			logger.error("The error while encrypting  the password.",
					e.fillInStackTrace());
			logger.debug("The error while encrypting  the password.",
					e.fillInStackTrace());
		} finally {
			byteKey = null;
			bytePwd = null;
			enc = null;
		}

		return encryptData;
	}

	public static String decrypt(String key, String password) throws Exception {
		String encryptData = "";
		byte[] byteKey = null;
		byte[] bytePwd = null;
		byte[] enc = null;

		try {
			byteKey = hex2byte(key);
			bytePwd = hex2byte(password);
			enc = CryptoUtil.desede(bytePwd, byteKey, Cipher.DECRYPT_MODE);

			encryptData = Util.hexString(enc);
		} catch (Exception e) {
			logger.error("The error while decrypting  the password.",
					e.fillInStackTrace());
			logger.debug("The error while decrypting  the password.",
					e.fillInStackTrace());
		} finally {
			byteKey = null;
			bytePwd = null;
			enc = null;
		}

		return encryptData;
	}

	public static byte[] hex2byte(String s) {
		if (s.length() % 2 == 0) {
			return hex2byte(s.getBytes(), 0, s.length() >> 1);
		} else {
			// Padding left zero to make it even size #Bug raised by tommy
			return hex2byte("0" + s);
		}
	}

	public static byte[] hex2byte(byte[] b, int offset, int len) {
		byte[] d = null;
		try {
			d = new byte[len];
			for (int i = 0; i < len * 2; i++) {
				int shift = i % 2 == 1 ? 0 : 4;
				d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
			}
		} catch (Exception e) {
			logger.error("The error while doing hex2byte.",
					e.fillInStackTrace());
			logger.debug("The error while doing hex2byte.",
					e.fillInStackTrace());
		} finally {

		}

		return d;
	}

	public static String rightPadding(String data, int len, char ch) {
		StringBuilder sb = null;

		try {
			sb = new StringBuilder();
			sb.append(data);
			for (int i = 0; i < len - data.length(); i++) {
				sb.append(ch);
			}
		} catch (Exception e) {
			logger.error("The error while doing rightPadding.",
					e.fillInStackTrace());
			logger.debug("The error while doing rightPadding.",
					e.fillInStackTrace());
		} finally {

		}

		return sb.toString();
	}

	public static String decrypt(String key, String password, int plen)
			throws Exception {
		String encryptData = "";
		String pwd = "";
		byte[] byteKey = null;
		byte[] bytePwd = null;
		byte[] enc = null;

		try {
			byteKey = hex2byte(key);
			bytePwd = hex2byte(password);
			enc = CryptoUtil.desede(bytePwd, byteKey, Cipher.DECRYPT_MODE);
			pwd = Util.hexString(enc);
			encryptData = pwd.substring(0, 4);
		} catch (Exception e) {
			logger.error("The error while doing string decrypt.",
					e.fillInStackTrace());
			logger.debug("The error while doing string decrypt.",
					e.fillInStackTrace());
		} finally {
			byteKey = null;
			bytePwd = null;
			enc = null;
			pwd = null;
		}

		return encryptData;
	}

	public static String add2Encrypt(String key, String encsupervisorpwd,
			String encadminpwd, char pad) throws Exception {
		String encryptData = "";
		String spwd = "";
		String apwd = "";
		byte[] byteKey = null;
		byte[] bytePwd = null;
		byte[] enc = null;
		try {
			byteKey = hex2byte(key);
			spwd = decrypt(key, encsupervisorpwd, 4);
			apwd = decrypt(key, encadminpwd, 4);
			bytePwd = hex2byte(rightPadding(apwd + spwd, 16, pad));
			enc = CryptoUtil.desede(bytePwd, byteKey, Cipher.ENCRYPT_MODE);

			encryptData = Util.hexString(enc);
		} catch (Exception e) {
			logger.error("The error while doing string add2Encrypt.",
					e.fillInStackTrace());
			logger.debug("The error while doing string add2Encrypt.",
					e.fillInStackTrace());
		} finally {
			byteKey = null;
			bytePwd = null;
			enc = null;
			spwd = null;
			apwd = null;
		}

		return encryptData;
	}

	public static void main(String[] args) {
		String key = "97206B46CE46376894703ECE161F31F2";
		// String key = "A568B16C7D510C9C51B1492E8691B159";
		String password = "119E5A25D0427A54B72DB85622C8A3D1";
		String encText = "";
		// String decryptPassword1 = "3CC768AB54D5B730";
		try {

			encText = encrypt(key, password, 'F');

			// System.out.println(encText);
			System.out.println(decrypt(key, "95139C61E35B3880"));
			// System.out.println(decrypt(key, decryptPassword1));

			// System.out.println( hex2byte(rightPadding(decrypt(key,
			// "3F1886765F1984DC", 4) + decrypt(key, "B95F3DB1856B01AD", 4), 16,
			// 'F')));

			/*System.out.println(add2Encrypt(key, "3F1886765F1984DC",
					"B95F3DB1856B01AD", 'F'));*/

		} catch (Exception e) {

		}
	}
}
