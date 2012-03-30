package org.cc.demo.rpc;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.util.ClientFactory;
import org.cc.demo.po.Ad;
import org.cc.demo.rpc.handler.Adder;


public class Client {

	/**
	 * new every time
	 * 
	 * @return
	 */
	public static XmlRpcClient get() {
		return get("127.0.0.1", 8081);
	}

	/**
	 * new every time
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public static XmlRpcClient get(String host, int port) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL("http://" + host + ":" + port
					+ "/xmlrpc"));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(5 * 1000);
		config.setReplyTimeout(20 * 1000);

		XmlRpcClient client = new XmlRpcClient();
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		client.setConfig(config);
		return client;

	}

	public static void main(String[] args) throws Exception {

		XmlRpcClient client = get();

		// make the a regular call
		Object[] params = new Object[] {};
		Ad result = (Ad) client.execute("AgeHandler.getAge", params);
		System.out.println("age : " + result.getId());

		// make a call using dynamic proxy (�ͻ��˷���˹��нӿ��࣬ʵ�������server��)
		ClientFactory factory = new ClientFactory(client);
		Adder adder = (Adder) factory.newInstance(Adder.class);

		int sum = adder.add(2, 4);
		System.out.println("2 + 4 = " + sum);
		Ad ad = new Ad();
		ad.setId(123L);
		String s = adder.add2("test add2", ad);
		System.out.println(s);
	}
}