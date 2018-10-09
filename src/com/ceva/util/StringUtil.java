package com.ceva.util;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public class StringUtil
{
  static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  public static void main(String[] args)
  {
    String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><txn-req><txn-amount>1000</txn-amount><credit-txn-req><expiry-date>0712</expiry-date><payment-type>SALE</payment-type><post-url>http://10.0.0.22:9080/umapi/post.jsp</post-url><ez-protect><bill-first-name>Jing</bill-first-name><ship-fax-num>66665555</ship-fax-num><product-format>text/plain</product-format><product-details quantity=\"1\"><name>abc</name><price>1</price><sku>sku</sku></product-details><ship-email>linjing@wizvision.com</ship-email><bill-phone-num>66667777</bill-phone-num><ship-initial>LJ</ship-initial><ship-city>Singapore</ship-city><bill-zip-code>600124</bill-zip-code><ship-coy-name>WizVision</ship-coy-name><bill-mobile-num>99998888</bill-mobile-num><ship-addr2>Singapore</ship-addr2><ship-country>Singapore</ship-country><ship-addr1>China</ship-addr1><bill-last-name>Lin</bill-last-name><bill-state>Singapore</bill-state><ship-zip-code>Singapore</ship-zip-code><bill-coy-name>WizVision</bill-coy-name><ship-phone-num>66667777</ship-phone-num><ship-first-name>Jing</ship-first-name><bill-city>Singapore</bill-city><bill-country>Singapore</bill-country><ship-mobile-num>99998888</ship-mobile-num><ship-last-name>Lin</ship-last-name><ship-state>Singapore</ship-state><bill-email>linjing@wizvision.com</bill-email><bill-fax-num>66665555</bill-fax-num><bill-addr2>Singapore</bill-addr2><bill-initial>LJ</bill-initial><bill-addr1>China</bill-addr1></ez-protect><cvv>232</cvv><pan>5655450000000000</pan><card-holder-name>Ah Hao</card-holder-name></credit-txn-req><currency-code>SGD</currency-code><tid>127.0.0.1</tid><submission-mode>B</submission-mode><merchant-cert-id>1</merchant-cert-id><payment-mode>CC</payment-mode><nets-mid>OT_M1</nets-mid><merchant-txn-ref>ref20051014017</merchant-txn-ref><success-url>http://10.0.0.22:9080/umapi/success.jsp</success-url><failure-url>http://10.0.0.22:9080/umapi/fail.jsp</failure-url><notify-url>http://10.0.0.22:9080/umapi/notify.jsp</notify-url></txn-req>";
    String cardno = "5655450000000000";
    System.out.println("Original msg: " + msg);
    System.out.println("Masked msg: " + maskInMsg(msg, cardno));
  }

  public static String combineURLwithQuery(String aURL, String aQuery)
  {
    if ((StringUtils.isEmpty(aURL)) || (StringUtils.isEmpty(aQuery)))
    {
      return aURL;
    }String tConnector = aURL.indexOf('?') == -1 ? "?" : "&";
    return aURL + tConnector + aQuery;
  }

  public static String convertToLowestUnit(String decAmt, int exp)
  {
    BigDecimal amtBig = new BigDecimal(decAmt);
    amtBig = amtBig.setScale(exp);
    amtBig = amtBig.movePointRight(exp);
    return amtBig.toString();
  }

  public static String convertToHighestUnit(String decAmt, int exp)
  {
    BigDecimal amtBig = new BigDecimal(decAmt);
    amtBig = amtBig.movePointLeft(exp);
    return amtBig.toString();
  }

  public static String mask(String cardno)
  {
    String masked = "";
    if ((cardno != null) && (cardno.length() > 0)) {
      StringBuffer sb = new StringBuffer(cardno);
      for (int i = 4; i < cardno.length() - 4; i++) {
        sb.replace(i, i + 1, "X");
      }
      masked = sb.toString();
    }
    return masked;
  }

  public static String maskInMsg(String msg, String cardno)
  {
    if ((msg != null) && (cardno != null)) {
      StringBuffer sb = new StringBuffer(msg);
      int iStart = msg.indexOf(cardno);
      int iEnd = iStart + cardno.length();
      for (int i = iStart + 4; i < iEnd - 4; i++) {
        sb.replace(i, i + 1, "X");
      }
      return sb.toString();
    }
    return msg;
  }

  public static String padStringRight(String str, int len)
  {
    StringBuffer res = new StringBuffer(len);
    res.append(str);
    for (int i = 0; i < len - str.length(); i++) {
      res.append(0);
    }
    return res.toString();
  }

  public static String padStringLeft(String str, int len)
  {
    StringBuffer res = new StringBuffer(len);

    for (int i = 0; i < len - str.length(); i++) {
      res.append(0);
    }
    res.append(str);
    return res.toString();
  }

  public static String padSpaceRight(String str, int len)
  {
    for (int i = str.length(); i < len; i++) {
      str = str + " ";
    }
    return str;
  }

  public static String readHex(byte[] b, int offset, int len)
  {
    StringBuffer s = new StringBuffer();
    for (int i = offset; i < offset + len; i++) {
      s.append(Integer.toHexString((b[i] & 0xF0) >>> 4)).append(Integer.toHexString(b[i] & 0xF));
    }
    return s.toString().toUpperCase();
  }

  public static String binaryToHex(byte[] bcd)
  {
    char[] dec = new char[bcd.length * 2];

    int i = 0; for (int j = 0; i < bcd.length; j += 2) {
      byte currentByte = bcd[i];

      dec[j] = Character.forDigit(currentByte >> 4 & 0xF, 16);
      dec[(j + 1)] = Character.forDigit(bcd[i] & 0xF, 16);

      i++;
    }

    return new String(dec);
  }

  public static byte[] hexToBinary(String dec)
  {
    byte[] bcd = new byte[dec.length() / 2];

    int i = dec.length() - 1; for (int j = bcd.length - 1; i >= 0; j--) {
      int lowbit = Character.digit(dec.charAt(i), 16);
      int highbit;
       if (i - 1 >= 0)
        highbit = Character.digit(dec.charAt(i - 1), 16);
      else {
        highbit = 0;
      }

      bcd[j] = (byte)(lowbit | highbit << 4);

      i -= 2;
    }

    return bcd;
  }

  public static String bcdTodec(byte[] bcd)
  {
    char[] dec = new char[bcd.length * 2];

    int i = 0; for (int j = 0; i < bcd.length; j += 2) {
      byte currentByte = bcd[i];

      dec[j] = Character.forDigit(currentByte >> 4 & 0xF, 10);
      dec[(j + 1)] = Character.forDigit(bcd[i] & 0xF, 10);

      i++;
    }

    return new String(dec);
  }

  public static boolean decTobcd(String dec, byte[] bcd)
  {
    if (dec.length() > bcd.length * 2) {
      return false;
    }
    int i = dec.length() - 1; for (int j = bcd.length - 1; i >= 0; j--) {
      int lowbit = Character.digit(dec.charAt(i), 10);
      int highbit;
       if (i - 1 >= 0)
        highbit = Character.digit(dec.charAt(i - 1), 10);
      else {
        highbit = 0;
      }

      if ((lowbit == -1) || (highbit == -1)) {
        return false;
      }
      bcd[j] = (byte)(lowbit | highbit << 4);

      i -= 2;
    }

    return true;
  }

  public static String toHexString(byte[] b) {
    StringBuffer sb = new StringBuffer(b.length * 2);
    for (int i = 0; i < b.length; i++)
    {
      sb.append(hexChar[((b[i] & 0xF0) >>> 4)]);

      sb.append(hexChar[(b[i] & 0xF)]);
    }
    return sb.toString();
  }

  public static byte[] fromHexString(String s)
  {
    int stringLength = s.length();

    if ((stringLength & 0x1) != 0) {
      throw new IllegalArgumentException("fromHexString requires an even number of hex characters");
    }

    byte[] b = new byte[stringLength / 2];

    int i = 0; for (int j = 0; i < stringLength; j++) {
      int high = charToNibble(s.charAt(i));
      int low = charToNibble(s.charAt(i + 1));
      b[j] = (byte)(high << 4 | low);

      i += 2;
    }

    return b;
  }

  private static int charToNibble(char c)
  {
    if (('0' <= c) && (c <= '9'))
      return c - '0';
    if (('a' <= c) && (c <= 'f'))
      return c - 'a' + 10;
    if (('A' <= c) && (c <= 'F')) {
      return c - 'A' + 10;
    }
    throw new IllegalArgumentException("Invalid hex character: " + c);
  }

  public static byte[] strTohex(String str, int len)
  {
    if (str.length() > len) {
      str = str.substring(0, len - 1);
    }
    StringBuffer hex = new StringBuffer(str);

    for (int i = str.length(); i < len; i++) {
      hex.append(' ');
    }

    return hex.toString().getBytes();
  }

  public static String[] split(String data, String delimiter) {
    String[] splited = data.split("\\" + delimiter);
    return splited;
  }

  public static String rtrim(String s)
  {
    StringBuffer stringbuffer = new StringBuffer(s);
    while ((stringbuffer.length() > 0) && (stringbuffer.charAt(stringbuffer.length() - 1) == ' ')) stringbuffer.deleteCharAt(stringbuffer.length() - 1);

    return stringbuffer.toString();
  }

  public static String ltrim(String s)
  {
    StringBuffer stringbuffer = new StringBuffer(s);
    while ((stringbuffer.length() > 0) && (stringbuffer.charAt(0) == ' ')) stringbuffer.deleteCharAt(0);

    return stringbuffer.toString();
  }

  public static String hexToChars(String s) {
    StringBuffer stringbuffer = new StringBuffer();
    for (int i = 0; i < s.length() - 1; i += 2) {
      stringbuffer.append((char)Integer.parseInt(s.substring(i, i + 2), 16));
    }

    return stringbuffer.toString();
  }

  public static byte[] fromHex(String s)
  {
    byte[] abyte0 = new byte[s.length() / 2];
    for (int i = 0; i < s.length() / 2; i++) {
      abyte0[i] = (byte)Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
    }

    return abyte0;
  }

  public static String toBinary(byte byte0)
  {
    StringBuffer stringbuffer = new StringBuffer();
    for (int i = 0; i < 8; i++) {
      stringbuffer.append((byte0 & 128 >> i) == 0 ? '0' : '1');
    }
    return stringbuffer.toString();
  }

  public static String toHex(byte byte0)
  {
    String s = "";
    s = s + Character.forDigit(byte0 >> 4 & 0xF, 16);
    s = s + Character.forDigit(byte0 & 0xF, 16);
    return s;
  }

  public static int findStringInArray(String s, String[] as)
  {
    for (int i = 0; i < as.length; i++) {
      if (s.equals(as[i]))
        return i;
    }
    return -1;
  }

  public static String genStringOfChars(char c, int i)
  {
    StringBuffer stringbuffer = new StringBuffer();
    for (int j = 0; j < i; j++) {
      stringbuffer.append(c);
    }
    return stringbuffer.toString();
  }

  public static boolean isNumeric(String s)
  {
    for (int i = 0; i < s.length(); i++) {
      if (!Character.isDigit(s.charAt(i)))
        return false;
    }
    return true;
  }

  public static String removeNonDigits(String s)
  {
    StringBuffer stringbuffer = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      if (Character.isDigit(s.charAt(i)))
        stringbuffer.append(s.charAt(i));
    }
    return stringbuffer.toString();
  }

  public static String[] createArrayFromTokens(String s, String s1)
  {
    StringTokenizer stringtokenizer = new StringTokenizer(s, s1);
    String[] as = new String[stringtokenizer.countTokens()];
    while (stringtokenizer.hasMoreTokens()) {
      as[(as.length - stringtokenizer.countTokens())] = stringtokenizer.nextToken();
    }
    return as;
  }

  public static String removeChars(String s, String s1)
  {
    StringBuffer stringbuffer = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      if (s1.indexOf(s.charAt(i)) == -1)
        stringbuffer.append(s.charAt(i));
    }
    return stringbuffer.toString();
  }

  public static String removeSubstring(String s, String s1, int i)
  {
    int j = s.indexOf(s1, i);
    if (j > -1)
      s = s.substring(0, j) + s.substring(j + s1.length(), s.length());
    return s;
  }

  public static String replaceString(String s, String s1, String s2)
  {
    StringBuffer stringbuffer = new StringBuffer(s);
    int j;
    for (int i = 0; (j = stringbuffer.toString().indexOf(s1, i)) > -1; i = j + s2.length())
    {
      stringbuffer.delete(j, j + s1.length());
      stringbuffer.insert(j, s2);
    }

    return stringbuffer.toString();
  }

  public static String encodeXML(String s)
  {
    StringBuffer stringbuffer = new StringBuffer();
    char[] ac = s.toCharArray();
    String s1 = null;
    int i = 0;
    int j = 0;
    for (; i < ac.length; i++) {
      switch (ac[i]) {
      case '<':
        s1 = "&lt;";
        break;
      case '>':
        s1 = "&gt;";
        break;
      case '\'':
        s1 = "&apos;";
        break;
      case '"':
        s1 = "&quot;";
        break;
      case '&':
        s1 = "&amp;";
      }

      if (s1 != null) {
        stringbuffer.append(ac, j, i - j);
        stringbuffer.append(s1);
        s1 = null;
        j = i + 1;
      }
    }

    if (j < ac.length)
      stringbuffer.append(ac, j, i - j);
    return stringbuffer.toString();
  }

  public static String encode(String s)
  {
    int i = s.length();
    StringBuffer stringbuffer = new StringBuffer(i);
    for (int j = 0; j < i; j++) {
      char c = s.charAt(j);
      switch (c) {
      case '\b':
        stringbuffer.append("\\b");
        break;
      case '\t':
        stringbuffer.append("\\t");
        break;
      case '\n':
        stringbuffer.append("\\n");
        break;
      case '\f':
        stringbuffer.append("\\f");
        break;
      case '\r':
        stringbuffer.append("\\r");
        break;
      case '"':
        stringbuffer.append("\\\"");
        break;
      case '\'':
        stringbuffer.append("\\'");
        break;
      case '\\':
        stringbuffer.append("\\\\");
        break;
      default:
        if ((c >= ' ') && (c <= '~')) {
          stringbuffer.append(c);
        }
        else {
          String s1 = Integer.toHexString(c).toUpperCase();
          stringbuffer.append("\\u");
          for (int k = 4 - s1.length(); k > 0; k--) {
            stringbuffer.append('0');
          }
          stringbuffer.append(s1);
        }
      }
    }

    return stringbuffer.toString();
  }

  public static String decode(String s)
  {
    int i = s.length();
    StringBuffer stringbuffer = new StringBuffer(i);
    for (int j = 0; j < i; j++)
    {
      char c;
      if ((c = s.charAt(j)) == '\\') {
        j++; c = s.charAt(j);
        switch (c) {
        case 'b':
          stringbuffer.append('\b');
          break;
        case 't':
          stringbuffer.append('\t');
          break;
        case 'n':
          stringbuffer.append('\n');
          break;
        case 'f':
          stringbuffer.append('\f');
          break;
        case 'r':
          stringbuffer.append('\r');
          break;
        case '"':
        case '\'':
        case '\\':
          stringbuffer.append(c);
          break;
        case 'u':
          String s1 = s.substring(j + 1, j + 5);
          j += 4;
          try {
            int k = Integer.parseInt(s1, 16);
            stringbuffer.append((char)k);
          } catch (NumberFormatException numberformatexception) {
            throw new IllegalArgumentException("Invalid esacpe: \\u" + s1);
          }

        case '0':
        case '1':
        case '2':
        case '3':
          String s2 = s.substring(j, j + 3);
          j += 2;
          try {
            int l = Integer.parseInt(s2, 8);
            stringbuffer.append((char)l);
          } catch (NumberFormatException numberformatexception1) {
            throw new IllegalArgumentException("Invalid escape: \\" + s2);
          }

        default:
          throw new IllegalArgumentException("Invalid escape: \\" + c);
        }
      } else {
        stringbuffer.append(c);
      }
    }

    return stringbuffer.toString();
  }

  public static String capitalize(String s)
  {
    char c = s.charAt(0);
    char c1 = Character.toTitleCase(c);
    if (c == c1) {
      return s;
    }
    StringBuffer stringbuffer = new StringBuffer(s);
    stringbuffer.setCharAt(0, c1);
    return stringbuffer.toString();
  }
}