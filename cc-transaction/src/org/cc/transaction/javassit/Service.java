/**
 * Service.java 11:59:38 AM Apr 27, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction.javassit;

import org.cc.transaction.Transactional;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 27, 2012
 */
@Transactional(readonly = true)
public class Service extends Dao{

	@Transactional
	@Override
	public void insert(Object o) {
		// TODO Auto-generated method stub
		super.insert(o);
	}
	
}
