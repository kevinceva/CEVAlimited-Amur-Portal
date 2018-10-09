package com.ceva.base.common.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBConnector_springds {

	private static Logger logger = Logger.getLogger(DBConnector.class);

	public static ApplicationContext context = new ClassPathXmlApplicationContext(
			"resource/dsdetails.xml");

	public static Connection getConnection() throws SQLException {

		logger.debug("Before getting the ConnectionBean object ........");
		ConnectionBean connectionBean = null;
		Connection connection = null;
		try {
			connectionBean = (ConnectionBean) context.getBean("cevaDataSource");
			connection = connectionBean.getDataSource().getConnection();
		} catch (Exception e) {
			logger.debug("Exception While Getting Connection Bean.... Please check the configurations....");
		}

		logger.debug("After getting the ConnectionBean object");

		return connection;
	}

}
