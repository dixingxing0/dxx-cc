package com.fujitsu.fnst.asm.utility;

import org.cc.core.asm.ClassVisitor;
import org.cc.core.asm.MethodVisitor;
import org.cc.core.asm.Opcodes;

/**
 * This class is used to filter the implement class. The implement class's
 * bytecode is used to combined to the origin class. But the <init> method must
 * be removed.
 * 
 * @author paul
 * 
 */
public class ImplementClassAdapter extends org.cc.core.asm.ClassVisitor {

	public ImplementClassAdapter(ClassVisitor cv) {
		super(Opcodes.ASM4,cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		if (AddImplementClassAdapter.INTERNAL_INIT_METHOD_NAME.equals(name)) {
			return null;
		}
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}

}
