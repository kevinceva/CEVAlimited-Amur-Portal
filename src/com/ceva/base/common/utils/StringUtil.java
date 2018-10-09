/*
 * StringUtil.java
 *
 * Created on April 16, 2008, 12:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.ceva.base.common.utils;

/*
 * StringUtil.java
 *
 * Created on October 31, 2007, 11:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


public class StringUtil {
    
    /** Creates a new instance of StringUtil */
    public StringUtil() {
    }
    
    public static String toUpperCase(String string) {
        //System.out.println(string+"in utils");
        if (string != null)
            string = string.toUpperCase();
        //System.out.println(string);
        return string;
    }
    
    /**
     * Check if the given string is either null or blank
     */
    public static boolean isNullOrEmpty(String string) {
        if (string == null)
            return true;
        if (string.trim().length() == 0)
            return true;
        if("na".equalsIgnoreCase(string.trim()) || "-".equals(string.trim()) || "_".equals(string.trim()))
            return true;
        if("null".equals(string))
             return true;
        
        return false;
    }
    
}
