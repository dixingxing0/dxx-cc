/**
 * Order.java 3:56:50 PM Apr 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc;

import org.cc.ioc.annotation.Inject;

/**
 * 用来测试的类：订单
 * 
 * @author dixingxing	
 * @date Apr 7, 2012
 */
public class Order {
	@Inject
	private Product product;
	
	@Inject
	private User user;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
