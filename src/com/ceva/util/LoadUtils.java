package com.ceva.util;

import java.util.ResourceBundle;

public class LoadUtils {
	
	/***
	 * Author : Ravi D
	 * Date MOdified : 02-12-2013
	 * Version : V1.0
	 * Class for loading Property files using Utils
	 */
	
	public static ResourceBundle getMyResource(String fileName){
		ResourceBundle rb=ResourceBundle.getBundle(fileName);
		return rb;
	}
	
}
