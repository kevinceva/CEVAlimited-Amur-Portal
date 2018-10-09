package com.ceva.user.security;


import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;

import com.ceva.user.utils.Util;


/*	
 	DATA FLOW FROM WEB
 	REQ FROM WEB --> USERID=REGISTER|USLIDN|TOKEN
	RES TO WEB   --> USERID=USLIDN|SERIALNO|TOKEN

	REQ FROM WEB --> USERID=VALIDATE|DBDATA(USLIDN|SERIALNO|TOKEN)
	Server Side Token Validation.
	RES FROM WEB --> USERID=[VALIDATEUSER / INVALIDATEUSER]|TOKEN
 */

/**
 * 
 * @author Rajkumar Pandey
 */
public class RSASecurity {


	private  static Logger logger=Logger.getLogger(RSASecurity.class);
	/**
	 * Encrypt Data
	 * @param data
	 * @throws IOException
	 */
	public static String encryptData(String data) throws IOException {

		logger.debug("\n----------------ENCRYPTION STARTED------------");

		logger.debug("Data Before Encryption :" + data);
		byte[] dataToEncrypt = data.getBytes();
		byte[] encryptedData = null;
		try {
			PrivateKey privateKey = readPrivateKey();
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			encryptedData = cipher.doFinal(dataToEncrypt);
			logger.debug("Encryted Data: " + encryptedData);

		} catch (Exception e) {
			 
			logger.debug("Exception in  encryptData: " + e.getMessage());
		}	

		logger.debug("----------------ENCRYPTION COMPLETED------------");		
		return Util.hexString(encryptedData); 
	}


	/**
	 * Encrypt Data
	 * @param data
	 * @throws IOException
	 */
	public static String decryptData(byte[] data) throws IOException {
		logger.debug("      ");
		logger.debug("----------------DECRYPTION STARTED------------");
		byte[] descryptedData = null;
		String decyData = null;

		PrivateKey privateKey = null;
		Cipher cipher = null;

		try {
			privateKey = readPrivateKey();
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			descryptedData = cipher.doFinal(data);
			decyData = new String(descryptedData);
			logger.debug("Decrypted Data: " + decyData);

		} catch (Exception e) {
			logger.debug("Exception in  decryptData: " + e.getMessage());
		}	
		logger.debug("----------------DECRYPTION COMPLETED------------");		
		return decyData;

	}

	/**
	 * Encrypt Data
	 * @param data
	 * @throws IOException
	 */
	public static String webDecryptData(byte[] data) throws IOException {
		logger.debug("      ");
		logger.debug("----------------DECRYPTION STARTED------------");
		byte[] descryptedData = null;
		PublicKey pubKey = null;
		Cipher cipher = null;
		
		String decyData = null;

		try {
			pubKey = readPublicKey();
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			descryptedData = cipher.doFinal(data);
			
			decyData = new String(descryptedData);
			logger.debug("Decrypted Data : " +decyData );

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}	
		logger.debug("----------------DECRYPTION COMPLETED------------");	
		return Util.hexString(descryptedData);

	}

	/**
	 * read Public Key From File
	 * @param fileName
	 * @return PublicKey
	 * @throws IOException
	 */
	private static PublicKey readWebPublicKey() throws IOException{
		BigInteger modulus = null;
		BigInteger exponent = null;

		RSAPublicKeySpec rsaPublicKeySpec = null;
		KeyFactory fact = null;
		PublicKey publicKey = null; 
		try {

			modulus = KeyConstants.PUBWEBMODULUS.getValue();
			exponent = KeyConstants.PUBWEBEXPONENT.getValue();

			rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			fact = KeyFactory.getInstance("RSA");
			publicKey = fact.generatePublic(rsaPublicKeySpec);

			return publicKey;

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

		return null;
	}
	/**
	 * read Public Key From File
	 * @param fileName
	 * @return PublicKey
	 * @throws IOException
	 */
	private static PublicKey readPublicKey() throws IOException{

		BigInteger modulus = null;
		BigInteger exponent = null;

		RSAPublicKeySpec rsaPublicKeySpec = null;
		KeyFactory fact = null;
		PublicKey publicKey = null;
		try {

			modulus = KeyConstants.PUBLOCALMODULUS.getValue();
			exponent = KeyConstants.PUBLOCALEXPONENT.getValue();

			rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			fact = KeyFactory.getInstance("RSA");
			publicKey = fact.generatePublic(rsaPublicKeySpec);

			return publicKey;

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

		return null;
	}
	/**
	 * read Public Key From File
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static PrivateKey readPrivateKey() throws IOException{
		BigInteger modulus = null;
		BigInteger exponent = null; 

		//Get Private Key
		RSAPrivateKeySpec rsaPrivateKeySpec = null;
		KeyFactory fact = null;
		PrivateKey privateKey = null;

		try {
			modulus = KeyConstants.PVTLOCALMODULUS.getValue();
			exponent = KeyConstants.PVTLOCALEXPONENT.getValue();


			//Get Private Key
			rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
			fact = KeyFactory.getInstance("RSA");
			privateKey = fact.generatePrivate(rsaPrivateKeySpec);

			return privateKey;

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

		return null;
	}

}
