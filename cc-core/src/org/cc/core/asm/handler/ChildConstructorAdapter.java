package org.cc.core.asm.handler;

import org.cc.core.asm.MethodVisitor;
import org.cc.core.asm.Opcodes;

public class ChildConstructorAdapter extends MethodVisitor {
	private String superClassName;

	public ChildConstructorAdapter(MethodVisitor mv,
			String superClassName) {
		super(Opcodes.ASM4, mv);
		this.superClassName = superClassName;
	}

	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
		// 调用父类的构造函数时
		if (opcode == Opcodes.INVOKESPECIAL && name.equals("<init>")) {
			owner = superClassName;
		}
		super.visitMethodInsn(opcode, owner, name, desc);// 改写父类为
															// superClassName
	}
}