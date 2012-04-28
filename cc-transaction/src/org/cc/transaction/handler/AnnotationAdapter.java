/**
 * AnnotationAdapter.java 2:25:37 PM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction.handler;

import org.cc.core.asm.AnnotationVisitor;
import org.cc.core.asm.Opcodes;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 26, 2012
 */
public class AnnotationAdapter extends AnnotationVisitor {

	/**
	 * constructor description
	 * 
	 * @param api
	 * @param av
	 */
	public AnnotationAdapter(int api, AnnotationVisitor av) {
		super(api, av);
		// TODO Auto-generated constructor stub
	}

	/**
	 * constructor description
	 * 
	 * @param api
	 */
	public AnnotationAdapter(int api) {
		super(api);
		// TODO Auto-generated constructor stub
	}

	public AnnotationAdapter() {
		super(Opcodes.ASM4);
	}
	
	@Override
	public void visit(String name, Object value) {
		System.out.println(name + value);
//		super.visit(name, value);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc) {
		System.out.println("visitAnnotation");
		return super.visitAnnotation(name, desc);
	}

	@Override
	public AnnotationVisitor visitArray(String name) {
		System.out.println("name");
		// TODO Auto-generated method stub
		return super.visitArray(name);
	}

	@Override
	public void visitEnd() {
		System.out.println("visitEnd");
		// TODO Auto-generated method stub
		super.visitEnd();
	}

	@Override
	public void visitEnum(String name, String desc, String value) {
		System.out.println("visitEnum");
		// TODO Auto-generated method stub
		super.visitEnum(name, desc, value);
	}

	
}
