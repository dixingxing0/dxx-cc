/**
 * RoleService.java 12:00:23 PM Apr 27, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction.javassit;
/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 27, 2012
 */
public class RoleService extends Service{
	public String executeOuter(String name) {
		System.out.println("executeOuter call super.query()");
		return query();
	}

	@Override
	public String query() {
		System.out.println("execute (RoleService): query");
		return "query result (RoleService)";
	}
}
