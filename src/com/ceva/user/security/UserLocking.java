package com.ceva.user.security;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;

import com.ceva.user.utils.Util;

public class UserLocking {
	static char[] hexval = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

	private static Logger logger = Logger.getLogger(UserLocking.class);

	public static String getRandomValue() {
		Random random = null;
		StringBuilder sb = null;
		try {
			random = new Random();
			sb = new StringBuilder(100);
			for (int i = 0; i < 12; i++) {
				sb.append(hexval[random.nextInt(15)]);
			}
			return sb.toString();

		} catch (Exception e) {
			logger.debug("Exception in getRandomValue ::: " + e.getMessage());
		}

		return null;
	}

	public static String getSystemMac() {
		StringBuilder sb = null;
		InetAddress ip = null;
		NetworkInterface network = null;

		byte[] mac = null;

		try {

			sb = new StringBuilder();
			ip = InetAddress.getLocalHost();
			logger.debug("Current IP address : " + ip.getHostAddress());

			network = NetworkInterface.getByInetAddress(ip);

			mac = network.getHardwareAddress();

			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : ""));
			}

		} catch (UnknownHostException e) {
			logger.debug("UnknownHostException in getSystemMac ::: "
					+ e.getMessage());
		} catch (SocketException e) {
			logger.debug("SocketException in getSystemMac ::: "
					+ e.getMessage());
		} finally {
			ip = null;
			network = null;
			mac = null;
		}

		return sb.toString();
	}

	private static String getSerialNo() {
		String sno = null;
		String property = null;
		String serial = null;

		Process process = null;
		Scanner sc = null;
		try {
			process = Runtime.getRuntime().exec(
					new String[] { "wmic", "bios", "get", "serialnumber" });
			process.getOutputStream().close();
			sc = new Scanner(process.getInputStream());
			property = sc.next();
			serial = sc.next();
			sno = serial;
		} catch (IOException e) {

		} finally {
			if (sc != null)
				sc.close();
			property = null;
			process = null;
			sc = null;
		}
		return sno;
	}

	public static String decrypt(String key, String password) throws Exception {

		byte[] byteKey = hex2byte(key);
		byte[] bytePwd = hex2byte(password);
		byte[] enc = CryptoUtil.desede(bytePwd, byteKey, Cipher.DECRYPT_MODE);

		return Util.hexString(enc);
	}

	public static String tmkEncrypt(String key, String password)
			throws Exception {

		byte[] byteKey = hex2byte(key);
		byte[] bytePwd = hex2byte(password);
		byte[] enc = CryptoUtil.desede(bytePwd, byteKey, Cipher.ENCRYPT_MODE);

		return Util.hexString(enc);
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
		byte[] d = new byte[len];
		for (int i = 0; i < len * 2; i++) {
			int shift = i % 2 == 1 ? 0 : 4;
			d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
		}
		return d;
	}

	public static void main(String[] args) {
		logger.debug("hello mac :: "+getSystemMac());
		logger.debug(getSerialNo());
	}

	public static String createLockingData(Map<String, String> data) {

		StringBuilder sb = new StringBuilder();

		sb.append(data.get("USERNAME")).append("=").append(getRandomValue());
		sb.append(data.get("USLIDN")).append("|");
		sb.append(getSerialNo());

		return sb.toString();
	}
}
