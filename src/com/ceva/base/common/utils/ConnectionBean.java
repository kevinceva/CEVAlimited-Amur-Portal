package com.ceva.base.common.utils;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConnectionBean {

	@Autowired
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static void main(String[] args) throws SQLException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"resource/dsdetails.xml");
		ConnectionBean connectionBean = (ConnectionBean) context
				.getBean("cevaDataSource");

		System.out.println(connectionBean.getDataSource().getConnection());

	}

}
