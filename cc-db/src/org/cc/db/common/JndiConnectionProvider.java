/**
 * ConnectionProvider.java 12:00:04 PM Apr 11, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.common;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.cc.core.common.Exceptions;

/**
 * 
 * 
 * @author dixingxing
 * @date Apr 11, 2012
 */
public class JndiConnectionProvider extends AbstractConnectionProvider {
	private DataSource ds;

	/**
	 * 
	 * @param jndiName
	 */
	public JndiConnectionProvider(String jndiName) {
		super();
		try {
			Context ic = new InitialContext();
			ds = (DataSource) ic.lookup(jndiName);
		} catch (Exception e) {
			Exceptions.uncheck(e);
		}
	}

	@Override
	public Connection createConn() {
		try {
			return ds.getConnection();
		} catch (Exception e) {
			Exceptions.uncheck(e);
			return null;
		}
	}

}