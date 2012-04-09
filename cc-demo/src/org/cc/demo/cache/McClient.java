/**
 * CacheUtils.java 9:49:57 AM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;

import org.apache.log4j.Logger;
import org.cc.core.CcException;

import com.google.code.yanf4j.core.impl.StandardSocketOption;

/**
 * xmemecached client
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
public class McClient {
	
	private static final Logger LOG = Logger.getLogger(McClient.class);

	private static final long TIME_OUT = 5000L;
	/** 接收缓存区，默认64K */
	private static final int SO_RCVBUF = 128 * 1024;
	/** 发送缓冲区，默认为16K */
	private static final int SO_SNDBUF = 32 * 1024;
	private static final int CONNECTION_POOL_SIZE = 2;
	/** 默认如果连接超过5秒没有任何IO操作发生即认为空闲并发起心跳检测 */
	private static final long SESSION_IDLE_TIMEOUT = 3000L;
	/** 默认是150，缩小到50 */
	private static final int MERGE_FACTOR = 50;
	
	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final int SERVER_PORT= 11211;
	

	private static MemcachedClient cache;

	static {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(getAddresses());
		
		builder.setSocketOption(StandardSocketOption.SO_RCVBUF, SO_RCVBUF);  
		builder.setSocketOption(StandardSocketOption.SO_SNDBUF, SO_SNDBUF); 
		// 启用nagle算法，提高吞吐量，默认关闭
		builder.setSocketOption(StandardSocketOption.TCP_NODELAY, false); 
		builder.setConnectionPoolSize(CONNECTION_POOL_SIZE);
		
		builder.setCommandFactory(new TextCommandFactory());
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		builder.setTranscoder(new SerializingTranscoder());

		builder.getConfiguration().setSessionIdleTimeout(SESSION_IDLE_TIMEOUT); 
		builder.getConfiguration().setStatisticsServer(true);

		try {
			cache = builder.build();
			cache.setOpTimeout(TIME_OUT);

			cache.setMergeFactor(MERGE_FACTOR); 
			// 关闭合并buffer的优化
			cache.setOptimizeMergeBuffer(false); 
			cache.setEnableHeartBeat(false);
		} catch (Exception e) {
			throw new CcException("初始化xmemcached异常",e);
		}
	}
	
	public McClient() {}

	/**
	 * 动态添加memcached 节点
	 * 
	 * @param host
	 * @param port
	 */
	public static void addServer(String host, int port) {
		try {
			cache.addServer(host, port);
		} catch (IOException e) {
			throw new CcException("添加节点失败",e);
		}
	}

	/**
	 * 默认的memcached 服务器地址
	 *  
	 * @return
	 */
	private static List<InetSocketAddress> getAddresses() {
		List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
		list.add(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
		return list;
	}

	/**
	 * 放入缓存
	 * 
	 * @param key
	 * @param exp
	 *            秒
	 * @param value
	 *            要实现Serializable
	 */
	public void set(final String key, final int exp, final Object value) {
		if (key == null || value == null) {
			return;
		}
		try {
			cache.set(key, exp, value, TIME_OUT);
			LOG.debug("set into memcached " + key);
		} catch (Exception e) {
			throw new CcException(e);
		}
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String key) {
		if (key == null) {
			return null;
		}
		T o = null;
		try {
			o = (T)cache.get(key, TIME_OUT);
			LOG.debug("get from memcached " + key);
		} catch (Exception e) {
			throw new CcException(e);
		}
		return (T) o;
	}

	/**
	 * 删除
	 * 
	 * @param key
	 */
	public void delete(final String key) {
		try {
			cache.delete(key);
		} catch (Exception e) {
			throw new CcException(e);
		}
	}
	
}
