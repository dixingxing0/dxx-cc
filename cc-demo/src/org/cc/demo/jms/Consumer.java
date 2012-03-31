/**
 * Consumer.java 2:43:46 PM Feb 14, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.cc.demo.common.constant.Constant;
import org.fusesource.stomp.jms.StompJmsConnectionFactory;

/**
 * jms 消费者
 * 
 * @author dixingxing
 * @date Feb 14, 2012
 */
public final class Consumer {
	private static final Logger LOG = Logger.getLogger(Consumer.class);
	
	private Consumer() {}
	
	public static void main(String[] args) throws JMSException, InterruptedException{
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://localhost:61613");

		Connection connection = null;
		MessageConsumer consumer = null;
		Session session = null;
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("myTest");
			connection = factory.createConnection("admin", "password");
			consumer = session.createConsumer(queue);
			
			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					try {
						LOG.debug(((TextMessage) message).getText());
					} catch (JMSException e) {
						LOG.error(null, e);
					}
				}
			});
			connection.start();
			Thread.sleep(Constant.TIME_HOUR);
		} finally {
			consumer.close();
			session.close();
			connection.close();
		}
	}
}
