package com.ceva.util;

import java.util.Random;

import net.sf.json.JSONArray;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

public class CommonUtil {

	public static Logger logger = Logger.getLogger(CommonUtil.class);
	public static String outputString = null;

	/** Method For Return 6 Digits Random Value **/
	public static String getRandomInteger() {
		int aStart = 100000;
		int aEnd = 999999;
		Random aRandom = new Random();
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * aRandom.nextDouble());
		Long randomNumber = (Long) (fraction + aStart);
		return randomNumber.toString();
	}

	public static String b64_sha256(String inputString) {
		if (inputString != null) {
			outputString = Base64.encodeBase64String(
					DigestUtils.sha256(inputString)).trim();
			logger.debug("b64_sha256 outputString::" + outputString);
		} else {
			logger.debug("Input String Missing for b64_sha256");
		}
		outputString = outputString.substring(0, outputString.length() - 1);
		return outputString;
	}

	public static String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(8).toUpperCase();
	}

	public static String generatePin(int size) {
		return RandomStringUtils.randomNumeric(size);
	}

	public static void main(String args[]) {
		String output = b64_sha256("Pass12!@");
		System.out.println(output);
		System.out.println(generatePassword());
		
		Object[] data = new Object[2];
		data[0] = "A";
		data[1] = 2;
		
		JSONArray jsonArr = new JSONArray();
		
		jsonArr.add(data);
		 
		System.out.println(jsonArr);
	}
}
