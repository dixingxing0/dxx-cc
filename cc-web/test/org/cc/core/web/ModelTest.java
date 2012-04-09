/**
 * ModelTest.java 11:20:27 AM Apr 1, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web;

import static org.junit.Assert.*;

import org.cc.web.Model;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 1, 2012
 */
public class ModelTest {
	private Model model;
	
	@Before
	public void setUp() {
		model = new Model();
	}

	/**
	 * Test method for {@link org.cc.web.Model#isDefaultKey(java.lang.String)}.
	 */
	@Test
	public void testIsDefaultKey() {
		assertTrue(model.isDefaultKey("defaultKey_object"));
		assertTrue(!model.isDefaultKey("someobject"));
	}

	/**
	 * Test method for {@link org.cc.web.Model#addAttribute(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testAddAttributeStringObject() {
		model.addAttribute("key0", "object0");
		assertEquals("object0", model.get("key0"));
	}

	/**
	 * Test method for {@link org.cc.web.Model#addAttribute(java.lang.Object)}.
	 */
	@Test
	public void testAddAttributeObject() {
		model.addAttribute("object0");
		model.addAttribute("object1");
		
		assertEquals(2 , model.size());
		for(String key : model.keySet()) {
			assertTrue(model.isDefaultKey(key));
		}
	}

	/**
	 * Test method for {@link org.cc.web.Model#containsAttribute(java.lang.String)}.
	 */
	@Test
	public void testContainsAttribute() {
		model.addAttribute("key0", "object0");
		assertTrue(model.containsAttribute("key0"));
		assertTrue(!model.containsAttribute("key1"));
	}

}
