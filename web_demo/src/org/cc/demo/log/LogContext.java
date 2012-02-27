/**
 * LogContext.java 6:10:16 PM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.log;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.cc.core.web.WebMethod;
import org.cc.demo.util.IpHelper;


/**
 * 
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
@XmlRootElement(name = "LogContext")
public class LogContext {
	private final static Logger logger = Logger.getLogger(LogContext.class);
	@XmlElement(name = "User")
	List<LogUser> allUsers = new ArrayList<LogUser>();

	@XmlElement(name = "Log")
	List<Log> logs = new ArrayList<Log>();

	private static LogContext c = null;

	private static Map<String, Log> map = new HashMap<String, Log>();

	/**
	 * 根据xml配置文件，初始化log context
	 * 
	 * @return
	 */
	static {
		if (c == null) {
			try {
				URL url = LogContext.class.getClassLoader().getResource(
						"log.xml");
				JAXBContext context = JAXBContext.newInstance(LogContext.class);
				FileReader fr = new FileReader(url.getPath());
				Unmarshaller um = context.createUnmarshaller();
				c = (LogContext) um.unmarshal(fr);
				String ip = IpHelper.LOCAL_IP;
				for (Log log : c.logs) {
					log.init(c.allUsers, ip);
					logger.debug("初始化log：" + log.method);
					map.put(log.method, log);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取方法(功能)配置的log信息
	 * 
	 * @param methodName
	 * @return
	 */
	public static Log getLog(WebMethod m) {
		String methodName = m.handler.getClass().getName() + "."
				+ m.method.getName();
		return map.get(methodName);
	}

	private LogContext() {
	}

	public static void main(String[] args) throws Exception {
		URL url = LogContext.class.getClassLoader().getResource("log.xml");

		System.out.println(url.getPath());
		// JAXBContext context = JAXBContext.newInstance(LogContext.class);
		// 下面代码演示将对象转变为xml
		// Marshaller m = context.createMarshaller();
		// Context c = new Context();
		// FileWriter fw = new FileWriter("E:\\c.xml");
		// m.marshal(c, fw);

		// 下面代码演示将上面生成的xml转换为对象
		// FileReader fr = new FileReader(url.getPath());
		// Unmarshaller um = context.createUnmarshaller();
		// LogContext c = (LogContext) um.unmarshal(fr);
		for (Log log : c.logs) {
			System.out.println(log);
		}
	}
}
