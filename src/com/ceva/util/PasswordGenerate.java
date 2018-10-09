package com.ceva.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.log4j.Logger;

public class PasswordGenerate
{
  protected static final Logger logger = Logger.getLogger(PasswordGenerate.class);

  public static String generatePassword() {
    char[] alpha = { 
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
      'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
      'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
      'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
      'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
      'y', 'z' };

    char[] spl = { 
      '!', '#', '$', '^', '%' };

    Random rm1 = new Random();
    int opt = 0;
    String result = "";
    StringBuffer sb = new StringBuffer();
    int min = 8;
    for (int i = 0; i < min; i++) {
      opt = rm1.nextInt(9);
      sb = sb.append(String.valueOf(opt));
      i++;
      opt = rm1.nextInt(5);
      sb = sb.append(spl[opt]);
      i++;
      opt = rm1.nextInt(52);
      sb = sb.append(alpha[opt]);
    }

    sb = sb.reverse();
    result = sb.toString();
    sb = null;
    alpha = (char[])null;
    spl = (char[])null;
    return result;
  }

  public static String generateToken(String sessionid)
  {
    return sessionid + System.currentTimeMillis() + generatePassword();
  }

  public static String hashing(String plain)
  {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA");
    }
    catch (NoSuchAlgorithmException nsae) {
      logger.error("[hashing] [Exception] " + nsae.toString());
      nsae.printStackTrace(System.err);
      throw new Error("load SHA algo fail", nsae);
    }
    md.update(plain.getBytes());
    byte[] raw = md.digest();
    StringBuffer strArray = new StringBuffer();
    strArray.append("");
    for (int x = 0; x < raw.length; x++) {
      int b = raw[x] >>> 255;
      if (b < 16) {
        strArray.append("0");
        strArray.append(Integer.toHexString(b).toUpperCase());
      } else {
        strArray.append(Integer.toHexString(b).toUpperCase());
      }
    }

    return strArray.toString();
  }

  public static String sendSMS(String url, int port, String from, String to, String plainText) {
    String reqXML = "<message><from>" + from + "</from>" + "<to>" + to + "</to>" + "<text>" + plainText + "</text>" + "</message>";
    String response = "";
    try {
      Socket socket = new Socket(url, port);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      out.println(reqXML);
      response = in.readLine();
      System.out.println("Response from SMS Listener.." + response);
      socket.close();
    }
    catch (Exception e)
    {
      logger.error("[sendSMS] [Exception] " + e.toString());
    }
    return response;
  }
  public static String encodeURL(String url) {
    try {
      return URLEncoder.encode(url, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      logger.error("[encodeURL] [Exception] " + e.toString());

       
    }
    return null;
  }
  public static String makeMask(String data) {
    if (data != null) {
      data = StringUtil.mask(data);
    }

    return data;
  }

  public static String ByteArrayToHexString(byte[] byteArray)
  {
    StringBuffer strArray = new StringBuffer();
    strArray.append("");
    for (int x = 0; x < byteArray.length; x++)
    {
      int b = byteArray[x] & 0xFF;
      if (b < 16)
      {
        strArray.append("0");
        strArray.append(Integer.toHexString(b).toUpperCase());
      }
      else
      {
        strArray.append(Integer.toHexString(b).toUpperCase());
      }
    }
    return strArray.toString();
  }
}