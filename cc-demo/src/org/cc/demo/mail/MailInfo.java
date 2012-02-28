package org.cc.demo.mail;

import java.util.Properties;

/**
 * 发送邮件需要使用的基本信息
 */
public class MailInfo {
	// 发送邮件的服务器的IP和端口
	private String mailServerHost = "smtp.yeah.net";
	private String mailServerPort = "25";
	// 登陆邮件发送服务器的用户名和密码
	private String userName = "dxxtest@yeah.net";
	private String password = "dxxtest123";
	// 邮件发送者的地址
	private String fromAddress = "dxxtest@yeah.net";

	// ////////////////////////////////////
	// 邮件接收者的地址
	private String toAddress;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;

	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", "true");
		return p;
	}

	public MailInfo(String to, String subject, String content) {
		this.toAddress = to;
		this.subject = subject;
		this.content = content;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}
}