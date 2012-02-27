package rpc;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import rpc.handler.Adder;
import rpc.handler.AdderImpl;

/**
 * run xmlrpc server
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
public class Server {
	private static final int port = 8081;

	public static void main(String[] args) throws Exception {
		WebServer webServer = new WebServer(port);

		XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

		PropertyHandlerMapping phm = new PropertyHandlerMapping();
		phm.load(Thread.currentThread().getContextClassLoader(),
				"rpc.properties");

		phm.addHandler(Adder.class.getName(), AdderImpl.class);
		xmlRpcServer.setHandlerMapping(phm);

		XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer
				.getConfig();
		serverConfig.setEnabledForExtensions(true);
		serverConfig.setContentLengthOptional(false);
		webServer.start();
		System.out.println("xmlRpcServer is running !");
	}
}