package org.cc.demo.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class Sender extends Thread {
	private final static Logger logger = Logger.getLogger(Sender.class);
	private final static String COMMA = ",";
	private MailInfo mi;
	private boolean isHtml = false;

	/**
	 * 邮件内容为普通文本
	 * 
	 * @param to
	 *            多个用逗号分隔
	 * @param subject
	 * @param content
	 * @return
	 */
	public static Sender textSender(String to, String subject, String content) {
		Sender s = new Sender(to, subject, content);
		s.isHtml = false;
		return s;
	}

	/**
	 * 
	 * 邮件内容为html
	 * 
	 * @param to
	 *            多个用逗号分隔
	 * @param subject
	 * @param content
	 * @return
	 */
	public static Sender htmlSender(String to, String subject, String content) {
		Sender s = new Sender(to, subject, content);
		s.isHtml = true;
		return s;
	}

	/**
	 * 请使用以下方法 <br/>{@link #textSender(String, String, String)} <br/>
	 * {@link #htmlSender(String, String, String)}
	 * 
	 * @param to
	 * @param subject
	 * @param content
	 */
	private Sender(String to, String subject, String content) {
		if (StringUtils.isBlank(to)) {
			logger.error("收件人不应该为空!");
		}
		// 这个类主要是设置邮件
		mi = new MailInfo(to, subject, content);
	}

	public void run() {
		sendMail();

	}

	static class MyAuthenticator extends Authenticator {
		String userName = null;
		String password = null;

		public MyAuthenticator() {
		}

		public MyAuthenticator(String username, String password) {
			this.userName = username;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password);
		}
	}

	/**
	 * 发送邮件
	 * 
	 */
	public boolean sendMail() {
		Properties pro = mi.getProperties();
		MyAuthenticator authenticator = new MyAuthenticator(mi.getUserName(),
				mi.getPassword());
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mi.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			String[] tos = mi.getToAddress().trim().split(COMMA);

			Address[] addresses = new Address[tos.length];
			for (int i = 0; i < tos.length; i++) {
				addresses[i] = new InternetAddress(tos[i]);
			}

			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipients(Message.RecipientType.TO, addresses);
			// 设置邮件消息的主题
			mailMessage.setSubject(mi.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			if (isHtml) {

				// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
				Multipart mainPart = new MimeMultipart();
				// 创建一个包含HTML内容的MimeBodyPart
				BodyPart html = new MimeBodyPart();
				// 设置HTML内容
				html.setContent(mi.getContent(), "text/html; charset=utf-8");
				mainPart.addBodyPart(html);
				// 将MiniMultipart对象设置为邮件内容
				mailMessage.setContent(mainPart);
			} else {
				// 设置邮件消息的主要内容
				String mailContent = mi.getContent();
				mailMessage.setText(mailContent);
			}
			// 发送邮件
			Transport.send(mailMessage);
			logger.debug(String.format("已发送邮件(to:%s)", mi.getToAddress()));
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(1);
		Sender sender = htmlSender("dixingxing@hc360.com,dixingxing@yeah.net",
				"邮箱标题", "邮箱内容<div style='color:green'>这是个div</div>");
		sender.start();
		System.out.println(2);
	}

}