/**
 * Product.java 3:56:58 PM Apr 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc;

import org.cc.ioc.annotation.Inject;
import org.cc.ioc.annotation.Ioc;

/**
 * 用来测试的类：商品
 * 
 * @author dixingxing	
 * @date Apr 7, 2012
 */

@Ioc
public class Product {
	private Long id;
	
	@Inject
	private ProductDetail productDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductDetail getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
	}
	
}
