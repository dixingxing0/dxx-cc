/**
 * ConnectionProvider.java 12:00:04 PM Apr 11, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.jdbc;

import java.sql.Connection;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 11, 2012
 */
public interface ConnectionProvider {
	Connection getConn() ;
}
