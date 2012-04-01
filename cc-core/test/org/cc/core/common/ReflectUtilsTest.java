/**
 * ReflectUtilsTest.java 4:08:41 PM Apr 1, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.cc.core.data.Child;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 1, 2012
 */
public class ReflectUtilsTest {
	private Child child;
	
	@Before
	public void setUp() {
		child = new Child();
		child.setId(1L);
		child.setName("name");
	}

	/**
	 * Test method for {@link org.cc.core.common.ReflectUtils#getFieldByName(java.lang.Object, java.lang.String)}.
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testGetFieldByName() throws IllegalArgumentException, IllegalAccessException {
		Field idField = ReflectUtils.getFieldByName(child, "id");
		idField.setAccessible(true);
		assertEquals(1L, idField.get(child));
		
		Field nameField = ReflectUtils.getFieldByName(child, "name");
		nameField.setAccessible(true);
		assertEquals("name", nameField.get(child));
		
		Field ageField = ReflectUtils.getFieldByName(child, "age");
		assertEquals(null, ageField);
		
		Field uuidField = ReflectUtils.getFieldByName(child, "UUID");
		assertEquals(0, uuidField.get(child));
		
	}

	/**
	 * Test method for {@link org.cc.core.common.ReflectUtils#getVariableFields(java.lang.Class)}.
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testGetVariableFields() throws IllegalArgumentException, IllegalAccessException {
		Field[] allFields = ReflectUtils.getVariableFields(Child.class);
		assertEquals(2, allFields.length);
		
	}

	/**
	 * Test method for {@link org.cc.core.common.ReflectUtils#getValueByFieldName(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testGetValueByFieldName() {
		assertEquals(1L,ReflectUtils.getValueByFieldName(child, "id"));
		assertEquals("name",ReflectUtils.getValueByFieldName(child, "name"));
		assertEquals(null,ReflectUtils.getValueByFieldName(child, "age"));
		assertEquals(0,ReflectUtils.getValueByFieldName(child, "UUID"));
	}

	/**
	 * Test method for {@link org.cc.core.common.ReflectUtils#set(java.lang.Object, java.lang.reflect.Field, java.lang.Object)}.
	 */
	@Test
	public void testSetObjectFieldObject() {
		ReflectUtils.set(child, ReflectUtils.getFieldByName(child, "id"), 2L);
		assertTrue(2L == child.getId());
	}

	/**
	 * Test method for {@link org.cc.core.common.ReflectUtils#get(java.lang.Object, java.lang.reflect.Field)}.
	 */
	@Test
	public void testGet() {
		assertEquals("name", ReflectUtils.get(child,  ReflectUtils.getFieldByName(child, "name")));
	}

	/**
	 * Test method for {@link org.cc.core.common.ReflectUtils#set(java.lang.Object, java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testSetObjectStringObject() {
		ReflectUtils.set(child,"id", 2L);
		assertTrue(2L == child.getId());
	}

}
