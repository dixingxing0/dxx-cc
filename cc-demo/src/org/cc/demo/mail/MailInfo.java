package org.cc.demo.mail;

import java.util.Properties;

public class MailInfo {
	private String mailServerHost = "smtp.yeah.net";
	private String mailServerPort = "25";
	private String userName = "dxxtest@yeah.net";
	private String password = "dxxtest123";
	private String fromAddress = "dxxtest@yeah.net";

	// ////////////////////////////////////
	private String toAddress;
	private String subject;
	private String content;

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