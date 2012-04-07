/**
 * ProductDetail.java 5:46:10 PM Apr 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc;

import org.cc.ioc.annotation.Ioc;

/**
 * 
 * 用来测试的类：商品规格
 * 
 * @author dixingxing	
 * @date Apr 7, 2012
 */
@Ioc
public class ProductDetail {
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
