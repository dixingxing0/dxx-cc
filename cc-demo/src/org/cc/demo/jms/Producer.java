/**
 * Producer.java 2:46:36 PM Feb 14, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.cc.demo.common.constant.Constant;
import org.fusesource.stomp.jms.StompJmsConnectionFactory;

/**
 * jms 生产者
 * 
 * @author dixingxing
 * @date Feb 14, 2012
 */
public final class Producer {
	private static final Logger LOG = Logger.getLogger(Producer.class);

	private Producer() {}
	
	/**
	 * 
	 * @throws JMSException 
	 * @throws JMSException
	 */
	public static void sendQueue(String m) throws JMSException {
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://localhost:61613");
		
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		try{
			connection = factory.createConnection("admin", "password");
			
			session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("myTest");
			producer = session.createProducer(queue);
	
			Message msg = session.createTextMessage(m);
			producer.send(msg);
			LOG.debug("已发送消息:" + m);
		} finally {
			producer.close();
			session.close();
			connection.close();
		}
	}

	public static void main(String[] args) throws JMSException, InterruptedException {
		for (int i = 0; i < 10; i++) {
			Thread.sleep(Constant.TIME_SECONDE);
			sendQueue("this is message " + i + "!");
		}
	}
}
