/**
 * ConnectionProvider.java 12:00:04 PM Apr 11, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.common;

import java.sql.Connection;
import java.sql.DriverManager;

import org.cc.core.common.Exceptions;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 11, 2012
 */
public class JdbcConnectionProvider extends AbstractConnectionProvider{
	private String driverClassName = null;
	private String userName = null;
	private String password = null;
	private String url = null;
	
	/**
	 * 
	 * @param driverClassName
	 * @param userName
	 * @param password
	 * @param url
	 */
	public JdbcConnectionProvider(String driverClassName, String userName,
			String password, String url) {
		super();
		this.driverClassName = driverClassName;
		this.userName = userName;
		this.password = password;
		this.url = url;
	}

	
	
	@Override
	public Connection createConn() {
		try {
			Class.forName(driverClassName);
			return DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			Exceptions.uncheck(e);
			return null;
		}
	}
}
