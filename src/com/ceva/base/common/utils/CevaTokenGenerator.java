package com.ceva.base.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;

public class CevaTokenGenerator {

	public static String getToken(String strData){
		byte[] hash = null;
		MessageDigest digest;
		String hexString = null;
		try {
			hexString = 	DigestUtils.md5Hex(strData.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			 
		}
		return hexString;
	}
	
	public static void main(String args[]){
		String applName="W701G000011";
		System.out.println(getToken(applName));
	}
}
