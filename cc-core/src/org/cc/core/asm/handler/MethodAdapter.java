/**
 * MethodAdapter.java 9:25:24 AM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.asm.handler;

import org.cc.core.asm.AnnotationVisitor;
import org.cc.core.asm.MethodVisitor;
import org.cc.core.asm.Opcodes;
import org.cc.core.asm.commons.AdviceAdapter;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 26, 2012
 */
public class MethodAdapter extends AdviceAdapter{
	private String name;
	private String desc;
	private String originalClassName;
	private Class<?> handlerClass;
	
	private boolean isTransactional;
	
	/**
	 * constructor description
	 * 
	 * @param api
	 * @param mv
	 * @param access
	 * @param name
	 * @param desc
	 */
	public MethodAdapter(int api, MethodVisitor mv, int access, String name,
			String desc,Class<?> handlerClass,String originalClassName) {
		super(api, mv, access, name, desc);
		this.name = name;
		this.desc = desc;
		this.handlerClass = handlerClass;
		this.originalClassName = originalClassName;
	}

	private String methodInfo() {
		return String.format("%s.%s|%s", this.originalClassName,this.name,this.desc);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		System.out.println(desc);
		isTransactional = desc != null && "Lorg/junit/After;".equals(desc);
		return super.visitAnnotation(desc, visible);
	}

	@Override
	protected void onMethodEnter() {
		
//		visitFieldInsn(GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");  
//		visitLdcInsn("Hello world!");  
//		visitMethodInsn(INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/String;)V"); 
		if(isTransactional) {
			visitLdcInsn(methodInfo()); // before方法的参数
			visitMethodInsn(Opcodes.INVOKESTATIC, handlerClass.getName().replace('.', '/'), 
					"before", "(Ljava/lang/String;)V"); 
		}
	}

	@Override
	protected void onMethodExit(int opcode) {
		if(isTransactional) {
			visitLdcInsn(methodInfo()); 
			visitMethodInsn(Opcodes.INVOKESTATIC,handlerClass.getName().replace('.', '/'), 
					"after", "(Ljava/lang/String;)V");
		}
	}

	
	
}
