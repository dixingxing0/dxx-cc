package org.cc.demo.web;

import javax.sql.DataSource;

import org.cc.demo.test.JettyUtils;
import org.cc.demo.test.Selenium2;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

/**
 * 功能测试基类.
 * 
 * 在整个测试期间启动一次Jetty Server, 并在每个TestCase执行前重新载入默认数据, 创建WebDriver.
 * 
 * @author calvin
 */
@Ignore
public class WebTestCase {
	protected static Server jettyServer;

	protected static DataSource dataSource;

	protected static Selenium2 s;
	protected static ChromeDriverService service ;
	@BeforeClass
	public static void startAll() throws Exception {
		JettyUtils.start();
		createSelenium();
		s.open(JettyUtils.BASE_URL);
	}

	@AfterClass
	public static void stopAll() throws Exception {
		quitSelenium();
	}


	/**
	 * 创建Selenium.
	 */
	protected static void createSelenium() throws Exception {
		String  baseRealClassPath= WebTestCase.class.getResource("/").getPath(); 
		System.setProperty("webdriver.chrome.driver",baseRealClassPath + "/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		s = new Selenium2(driver, JettyUtils.BASE_URL);
	}

	/**
	 * 退出Selenium.
	 */
	protected static void quitSelenium() {
		s.quit();
	}

	/**
	 * 登录管理员, 如果用户还没有登录.
	 */
	protected static void loginAsAdminIfNecessary() {
		s.open("/account/user");

		if ("Mini-Web示例:登录页".equals(s.getTitle())) {
			s.type(By.name("username"), "admin");
			s.type(By.name("password"), "admin");
			s.check(By.name("rememberMe"));
			s.click(By.id("submit"));
		}
	}

	/**
	 * 登录特定用户.
	 */
	protected static void login(String user, String password) {
		s.open("/logout");
		s.type(By.name("username"), user);
		s.type(By.name("password"), password);
		s.click(By.id("submit"));
	}
}