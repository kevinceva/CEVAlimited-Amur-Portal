package com.ceva.util;
import java.util.ResourceBundle;
 import org.apache.log4j.Logger;
 
 public class ReportsBundle
 {
   protected static Logger logger = Logger.getLogger("ReportBundle.class");
 
   public static String getValue(String key)
   {
     String value = null;
     ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
     value = resourceBundle.getString(key);
     logger.debug("[ReportBundle][getValue] the Key :: " + key + " and value :: " + value);
     return value;
   }
 }