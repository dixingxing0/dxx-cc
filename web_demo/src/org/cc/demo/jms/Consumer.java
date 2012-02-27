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
import org.fusesource.stomp.jms.StompJmsConnectionFactory;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 14, 2012
 */
public class Consumer {
	private final static Logger logger = Logger.getLogger(Consumer.class);

	private static Connection getConn() throws JMSException {
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://localhost:61613");

		Connection connection = factory.createConnection("admin", "password");
		return connection;
	}

	public static void main(String[] args) throws Exception {
		Connection connection = getConn();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("myTest");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				try {
					logger.debug(((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		connection.start();
		Thread.sleep(1000 * 10000L);
		consumer.close();
		session.close();
		connection.close();
	}
}
