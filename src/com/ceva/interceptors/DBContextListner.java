package com.ceva.interceptors;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.ceva.base.common.utils.DBConnector;

public class DBContextListner implements ServletContextListener {

	private static Logger logger = Logger.getLogger(DBContextListner.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		// ... First close any background tasks which may be using the DB ...
		// ... Then close any DB connection pools ...

		// Now De-register JDBC drivers in this context's ClassLoader:
		// Get the webapp's ClassLoader
		ClassLoader cl = null;
		// Loop through all drivers
		Enumeration<Driver> drivers = null;
		Driver driver = null;
		try {
			
			logger.info("|DBContextListner| Destroying connection.");
			//DBConnector.stop();
			logger.info("|DBContextListner| Bonecp Connection Destroyed.");

			cl = Thread.currentThread().getContextClassLoader();
			drivers = DriverManager.getDrivers();

			while (drivers.hasMoreElements()) {
				driver = drivers.nextElement();
				if (driver.getClass().getClassLoader() == cl) {
					// This driver was registered by the webapp's ClassLoader,
					// so
					// deregister it:
					try {
						logger.info(" |DBContextListner| Deregistering JDBC driver {" + driver
								+ "}");
						DriverManager.deregisterDriver(driver);
					} catch (SQLException ex) {
						logger.info(" |DBContextListner| Error deregistering JDBC driver {"
								+ driver + "} :: " + ex.getMessage());
					}
				} else {
					// driver was not registered by the webapp's ClassLoader and
					// may
					// be in use elsewhere
					logger.info(" |DBContextListner| Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader..."
							+ driver);
				}
			} 
		} catch (Exception e) {
			logger.info("|DBContextListner| Got Error While Destorying Connection .... "+ e.getMessage());
		} finally {
			 cl = null;
			 drivers = null;
			 driver = null; 
 		}

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
 		try {
			logger.info("|DBContextListner| Initilizing connection.");
			 new DBConnector();
			logger.info("|DBContextListner| connection Initilized.");
		} catch (Exception e) {
			logger.info("|DBContextListner| Got Excpetion While Initilizing Connection Pooling.... "+ e.getMessage());
		} finally {
 		}
		

	}

}
