/**
 * UserDao.java 7:01:35 PM Jan 17, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package service;

import java.sql.Connection;
import java.sql.SQLException;

import po.Ad;

import dao.Dao;

/**
 * “µŒÒ¿‡
 * 
 * @author dixingxing
 * @date Jan 17, 2012
 */
public class AdManager extends Dao<Ad> {
	private static final String sql = "update ad set state = ? where id = ?";

	/**
	 * just for test
	 * 
	 * @param id
	 * @param state
	 * @throws SQLException
	 */
	public void changeStateRollback(Long id, Long state) throws SQLException {
		Connection conn = getConn();
		conn.setAutoCommit(false);
		update(conn, sql, state, id);
		rollbackAndClose(conn);
	}

	/**
	 * just for test
	 * 
	 * @param id
	 * @param state
	 * @throws SQLException
	 */
	public void changeStateCommit(Long id, Long state) throws SQLException {
		Connection conn = getConn();
		conn.setAutoCommit(false);
		update(conn, sql, state, id);
		commitAndClose(conn);
	}
}
