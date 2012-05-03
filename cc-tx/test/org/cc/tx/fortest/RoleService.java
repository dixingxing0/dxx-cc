/**
 * RoleService.java 12:00:23 PM Apr 27, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.fortest;

import java.sql.Savepoint;

import org.cc.tx.Transactional;
import org.cc.tx.TxUtils;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 27, 2012
 */
public class RoleService extends Service{
	@SuppressWarnings("unused")
	private String field1 = "";
	
	public String executeOuter(Integer name) {
		System.out.println("executeOuter call super.query()");
		return query();
	}

	@Override
	public String query() {
		System.out.println("execute (RoleService): query");
		return "query result (RoleService)";
	}
	
	@Transactional
	public void nestedTest(float f1,double d4,long id, Integer i2, String s3) { 
		
		Savepoint sp = TxUtils.createSavepoint();
		TxUtils.rollbackToSavepoint(sp);
		
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	} 
	
}
