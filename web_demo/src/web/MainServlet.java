/**
 * MainServlet.java 5:05:10 PM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package web;

import jms.PerformanceLogProducer;
import log.Log;
import log.LogContext;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
@SuppressWarnings("serial")
public class MainServlet extends DispatcherServlet {
	private final static Logger logger = Logger.getLogger(MainServlet.class);

	@Override
	protected void afterProcess(WebMethod webMethod, long start) {
		long cost = costTime(start);
		Log log = LogContext.getLog(webMethod);
		if (log != null) {
			log.setCost(cost);
			PerformanceLogProducer.log(log);
		}
		logger.debug("方法" + webMethod.method.getName() + "耗时" + cost + "毫秒");
	}

	@Override
	protected void exceptionOccured(WebMethod webMethod, long start, Throwable e) {
		long cost = costTime(start);
		Log log = LogContext.getLog(webMethod);
		if (log != null) {
			log.setCost(cost);
			log.setError(e);
			PerformanceLogProducer.log(log);
		}
		logger.debug("出现异常耗时" + cost + "毫秒", e);
	}

	private long costTime(long start) {
		return (System.currentTimeMillis() - start);
	}

}
