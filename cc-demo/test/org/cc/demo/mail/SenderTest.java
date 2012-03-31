/**
 * SenderTest.java 2:01:27 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.mail;

import java.util.Date;

import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public class SenderTest {

	/**
	 * Test method for {@link org.cc.demo.mail.Sender#sendMail()}.
	 */
	@Test
	public void testSendMail() {
		String to = "dxx.mazzystar@yahoo.com.cn";
		Sender sender = Sender.htmlSender(to,
				"html", "邮箱内容<div style='color:green'>这是个div <br />generated at " +new Date()+ "</div>");
		sender.start();
		
		Sender sender2 = Sender.htmlSender(to,
				"text", "邮箱内容这是个文本邮件 \r\n generated at " +new Date()+ "</div>");
		sender2.start();
	}

}
