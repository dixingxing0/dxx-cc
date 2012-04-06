package org.cc.demo.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 
 * @author dixingxing	
 * @date Apr 6, 2012
 */
public class MemoTest extends WebTestCase {

	/**
	 * 查看列表
	 */
	@Test
	public void viewList() {
		s.click(By.linkText("查看memo示例"));
		WebElement addLink = s.findElement(By.id("add"));
		assertEquals("新增", addLink.getText());
	}
}
