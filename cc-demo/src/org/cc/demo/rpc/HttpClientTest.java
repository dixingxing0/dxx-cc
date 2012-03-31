package org.cc.demo.rpc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientTest {

	private static String link = "http://www.baidu.com";

	public static void main(String[] args) {
		long a = System.currentTimeMillis();
		useHttpURlConnection();
		long b = System.currentTimeMillis();
		System.out.println("use httpurlconnection: "+(b-a));
		long c = System.currentTimeMillis();
		useHttpClient();
		long d = System.currentTimeMillis();
		System.out.println("use httpclient: "+(d-c));
	}
	
	public static void useHttpURlConnection(){
		HttpURLConnection conn = null;
		URL url = null;
		String result = "";
		try {
			url = new java.net.URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.connect();

			InputStream urlStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
			String s = "";
			while ((s = reader.readLine()) != null) {
				result += s;
			}
			System.out.println(result);
			reader.close();
			urlStream.close();
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void useHttpClient(){
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(link);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		try {
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			byte[] responseBody = method.getResponseBody();
			System.out.println(new String(responseBody));
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}
}