package com.ceva.base.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;

public class AesUtil {
	 private static final int pswdIterations = 10;
		private static final int keySize =  128;
		
	public static void main(String[] args) throws Exception {
		/*String text = "Pandey";
		String pwdencyp = "kailash";
		System.out.println("Plain Text ["+text+"]");
	    String result1 = AesUtil.encrypt(text, pwdencyp, "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55", "12345678901234567890123456789012");
	    
	    System.out.println("Encrypted Text ["+result1+"]");
	     String result2 = AesUtil.decrypt(result1, pwdencyp, "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55", "12345678901234567890123456789012");
	     System.out.println("Plain Text ["+result2+"]");*/
	     
	     System.out.println("Plain Text ["+AesUtil.md5("kailash")+"]");
		
	
		
	}  
	
		
		
		
		
	public static String encrypt(String plainText, String password, String salt, String initializationVector) throws NoSuchAlgorithmException, NoSuchPaddingException, DecoderException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), Hex.decodeHex(salt.toCharArray()), pswdIterations, keySize);
        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(Hex.decodeHex(initializationVector.toCharArray())));
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes());
        return new Base64().encodeBase64String(encryptedTextBytes);
}                                                                                                                                                                                                                                                                                                                          
 
	public static String decrypt(String plainText, String password, String salt, String initializationVector) throws NoSuchAlgorithmException, NoSuchPaddingException, DecoderException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
		
			Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    IvParameterSpec ivParameterSpec = new IvParameterSpec(Hex.decodeHex(initializationVector.toCharArray()));
		    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        KeySpec spec = new PBEKeySpec(password.toCharArray(), Hex.decodeHex(salt.toCharArray()), pswdIterations, keySize);
		    SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		    decryptCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
		    byte[] output = decryptCipher.doFinal(new Base64().decode(plainText));
		    return bytes2String(output);

        
}  
	
	 private static String bytes2String(byte[] bytes) {
	        StringBuffer stringBuffer = new StringBuffer();
	        for (int i = 0; i < bytes.length; i++) {
	            stringBuffer.append((char) bytes[i]);
	        }
	        return stringBuffer.toString();
	    }
	 
	 
	 
	 public static String md5(String input) {
			
			String md5 = null;
			
			if(null == input) return null;
			
			try {
				
			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			//Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			//Converts message digest value in base 16 (hex) 
			md5 = new BigInteger(1, digest.digest()).toString(16);

			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			}
			return md5;
		}
	
}

