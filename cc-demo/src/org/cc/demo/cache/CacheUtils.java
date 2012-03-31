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
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;

import org.apache.log4j.Logger;
import org.cc.demo.po.Ad;


import com.google.code.yanf4j.core.impl.StandardSocketOption;

/**
 * xmemecached
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
public class CacheUtils {
	private static Logger LOG = Logger.getLogger(CacheUtils.class);
	private static final String CACHE_ERROR = "操作缓存出错";
	private static MemcachedClient cache;

	private static long TIME_OUT = 5000L;

	static {
		LOG.debug("---------初始化memcachedClient 开始---------");
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				getAddresses());
		builder.setSocketOption(StandardSocketOption.SO_RCVBUF, 128 * 1024); // 设置接收缓存区，默认64K
		builder.setSocketOption(StandardSocketOption.SO_SNDBUF, 32 * 1024); // 设置发送缓冲区，默认为16K
		builder.setSocketOption(StandardSocketOption.TCP_NODELAY, false); // 启用nagle算法，提高吞吐量，默认关闭
		builder.setConnectionPoolSize(2); //
		builder.setCommandFactory(new TextCommandFactory());
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		builder.setTranscoder(new SerializingTranscoder());

		builder.getConfiguration().setSessionIdleTimeout(10000L); // 默认如果连接超过5秒没有任何IO操作发生即认为空闲并发起心跳检测
		builder.getConfiguration().setStatisticsServer(true);

		try {
			cache = builder.build();
		} catch (Exception e) {
			LOG.error("初始化xmemcached client 失败", e);
		}
		cache.setOpTimeout(TIME_OUT);

		cache.setMergeFactor(50); // 默认是150，缩小到50
		cache.setOptimizeMergeBuffer(false); // 关闭合并buffer的优化
		cache.setEnableHeartBeat(false);

	}

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
			e.printStackTrace();
		}
	}

	/**
	 * 默认的memcached 服务器地址
	 *  
	 * @return
	 */
	private static List<InetSocketAddress> getAddresses() {
		List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
		list.add(new InetSocketAddress("127.0.0.1", 11211));
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
	public static void set(final String key, final int exp, final Object value) {
		if (key == null || value == null) {
			return;
		}
		try {
			cache.set(key, exp, value, TIME_OUT);
			LOG.debug("set into memcached " + key);
		} catch (Exception e) {
			LOG.error(CACHE_ERROR, e);
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(final String key) {
		if (key == null) {
			return null;
		}
		T o = null;
		try {
			o = (T)cache.get(key, TIME_OUT);
			LOG.debug("get from memcached " + key);
		} catch (Exception e) {
			LOG.error(CACHE_ERROR, e);
		}
		return (T) o;
	}

	/**
	 * 删除
	 * 
	 * @param key
	 */
	public static void delete(final String key) {
		try {
			cache.delete(key);
		} catch (Exception e) {
			LOG.error(CACHE_ERROR, e);
		}
	}

	public static void main(String[] args) throws MemcachedException,
			InterruptedException, TimeoutException {
		for (int i = 0; i < 200; i++) {
			CacheUtils.set("test_" + i, 15, "test_v_" + i);
		}
		for (int i = 0; i < 200; i++) {
			System.out.println(CacheUtils.get("test_" + i));
		}
		Ad ad = new Ad();
		ad.setName("ad_name");
		ad.setId(123L);
		CacheUtils.set("ad_test", 5, ad);
		System.out.println(CacheUtils.get("ad_test"));
	}
}
