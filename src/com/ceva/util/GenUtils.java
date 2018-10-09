package com.ceva.util;

import java.util.Calendar;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class GenUtils
{
  protected static Logger logger = Logger.getLogger(GenUtils.class);

  public static String getKeyValue(String resouceName, String key)
    throws Exception
  {
    String value = null;
    ResourceBundle bundle = ResourceBundle.getBundle(resouceName);
    value = bundle.getString(key);
    logger.debug("[GenUtils] [getValue] Key - Value : " + key + " - " + value);
    return value;
  }

  public static String getCurrentTime()
  {
     return Calendar.HOUR+ ":" + Calendar.MINUTE + ":" + Calendar.SECOND;
  }
}
