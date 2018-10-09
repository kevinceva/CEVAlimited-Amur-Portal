package com.ceva.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.io.IOException;
import org.apache.log4j.Logger;

public class LoadPropFile {

	public static Logger logger = Logger.getLogger(LoadPropFile.class);

	public static Properties generateResouce(String fileName) {

		Properties fileProp = null;
		FileInputStream fis = null;
		try {
			fileProp = new Properties();
			logger.debug("File Name Received From RequestGenerator][File Name ="
					+ fileName);
			fis = new FileInputStream(fileName);
			try {
				fileProp.load(fis);
			} catch (IOException e) {

			}

		} catch (FileNotFoundException e) {

		} finally {

			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		logger.debug("Before return Properties Object");
		return fileProp;
	}
}
