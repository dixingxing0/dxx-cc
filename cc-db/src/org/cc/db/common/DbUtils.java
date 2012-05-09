/**
 * Utils.java 2:49:35 PM May 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.common;

import static org.cc.db.JdbcConfig.getDriverClassName;
import static org.cc.db.JdbcConfig.getJndiDataSourceName;
import static org.cc.db.JdbcConfig.getPassword;
import static org.cc.db.JdbcConfig.getConnectionProvider;
import static org.cc.db.JdbcConfig.getTransactionProvider;
import static org.cc.db.JdbcConfig.getUrl;
import static org.cc.db.JdbcConfig.getUserName;

import org.apache.log4j.Logger;
import org.cc.core.CcException;
import org.cc.core.common.Strings;

/**
 * <p>读取{@link #ConnectionProvider}和{@link #txProvider}的工具类</p>
 * 
 * @author dixingxing	
 * @date May 9, 2012
 */
public final class DbUtils {
	private static final Logger LOG = Logger.getLogger(DbUtils.class);
	
	private static ConnectionProvider connProvider;
	
	private static TxProvider txProvider;
	
	private DbUtils() {}
	
	/**
	 * 
	 * <p>获取配置文件中配置的ConnectionProvider</p>
	 *
	 * @return
	 */
	public static ConnectionProvider getConnProvider() {
		if(connProvider != null) {
			return connProvider;
		}
		if (JdbcConnectionProvider.class.getName().equals(getConnectionProvider())) {
			LOG.debug("使用jdbcConnectionProvider");
			connProvider = new JdbcConnectionProvider(getDriverClassName(),getUserName(),getPassword(),getUrl());
		} else if (JndiConnectionProvider.class.getName().equals(getConnectionProvider())) {
			LOG.debug("使用jndicConnectionProvider");
			connProvider = new JndiConnectionProvider(getJndiDataSourceName());
		} else {
			throw new CcException("请配置属性connectionProvider");
		}
		return connProvider;
	}
	
	/**
	 * 
	 * <p>获取TxProvider</p>
	 *
	 * @return
	 */
	public static TxProvider getTxProvider() {
		if(txProvider != null) {
			return txProvider;
		}
		String txProviderName = getTransactionProvider();
		
		if(Strings.isBlank(txProviderName)) {
			return null;
		}
		try {
			txProvider = (TxProvider) Class.forName(txProviderName).newInstance();
		} catch (Exception e) {
			throw new CcException("请检查配置属性transactionProvider",e);
		} 
		return txProvider;
	}
}
