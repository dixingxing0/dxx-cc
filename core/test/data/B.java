/**
 * A.java 10:29:00 AM Feb 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package data;

import dao.annotation.Table;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 9, 2012
 */
@SuppressWarnings("serial")
@Table(name = "ad")
public class B extends A implements I {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
