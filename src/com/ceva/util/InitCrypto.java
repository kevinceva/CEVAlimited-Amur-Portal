package com.ceva.util;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ceva.crypto.utils.CryptoTool;

public class InitCrypto {
	protected static final Logger logger = Logger.getLogger(InitCrypto.class);

	public static void initializeCrypto(HttpServletRequest req) {
		String contextPath = req.getRealPath("/");
		String loc = null;
		String contextClasspath = null;
		Properties propConfig = new Properties();

		logger.debug("actual_path[" + contextPath + "]");

		try {

			logger.debug("Loading Constants");

			contextClasspath = contextPath + "WEB-INF/classes/";

			logger.debug("contextClasspath [" + contextClasspath + "]");
			propConfig.load(new FileInputStream(contextClasspath
					+ "auth.properties"));

			req.getSession().setAttribute("PATH", contextClasspath);
			loc = contextClasspath + propConfig.getProperty("KEYSTORE_PATH");
			logger.debug("Setting the values to CryptoTool.");

			CryptoTool.setKeystorePath(loc);
			CryptoTool.setAlgorithm(propConfig.getProperty("KEYSTORE_ALGO"));
			CryptoTool.setKeyAlias(propConfig.getProperty("KEY_ALIAS"));
			CryptoTool.setKeyPassword(propConfig.getProperty("KEYPASS"));
			CryptoTool.setKeystorePassword(propConfig
					.getProperty("KEYSTORE_PASS"));
			CryptoTool.cryptoTool();

			logger.debug("All the Variables Loaded To Crypto Successfully");
		} catch (Exception e) {
			logger.error("[Error] Unable to Load the Config Details "
					+ e.toString());
		}

		finally {
			propConfig = null;
		}
	}
}