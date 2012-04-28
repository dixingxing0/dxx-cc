package com.fujitsu.fnst.asm.utility;

import org.cc.core.asm.MethodVisitor;
import org.cc.core.asm.Opcodes;

/**
 * This class is used to modify the <init> method for enhanced class.
 * @author paul
 *
 */
public class ModifyInitMethodAdapter extends MethodVisitor {
	private String className;

	public ModifyInitMethodAdapter(MethodVisitor mv, String name) {
		super(Opcodes.ASM4,mv);
		this.className = name;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
		if (name.equals(AddImplementClassAdapter.INTERNAL_INIT_METHOD_NAME)) {
			mv.visitMethodInsn(opcode, className.replace(".", "/"), name, desc);
		}
	}

}
