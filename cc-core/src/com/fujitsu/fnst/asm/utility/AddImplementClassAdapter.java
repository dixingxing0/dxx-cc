package com.fujitsu.fnst.asm.utility;

import java.io.IOException;

import org.cc.core.asm.ClassReader;
import org.cc.core.asm.ClassVisitor;
import org.cc.core.asm.ClassWriter;
import org.cc.core.asm.MethodVisitor;
import org.cc.core.asm.Opcodes;

/**
 * This class is used to add implement classes' bytecode to the original class.
 * The new class is extends the original class,and name with
 * orginalClass+"$PROXY";
 * 
 * @author paul
 * 
 */
public class AddImplementClassAdapter extends ClassVisitor {
	public static final String INTERNAL_INIT_METHOD_NAME = "<init>";
	private ClassWriter classWriter;
	private Class<?>[] implementClasses;
	private String originalClassName;
	private String enhancedClassName;

	public AddImplementClassAdapter(String enhancedClassName,
			Class<?> targetClass, ClassWriter writer,
			Class<?>... implementClasses) {
		super(Opcodes.ASM4,writer);
		this.classWriter = writer;
		this.implementClasses = implementClasses;
		this.originalClassName = targetClass.getName();
		this.enhancedClassName = enhancedClassName;
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		cv.visit(version, Opcodes.ACC_PUBLIC,
				enhancedClassName.replace('.', '/'), signature, name,
				interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		if (INTERNAL_INIT_METHOD_NAME.equals(name)) {
			MethodVisitor mv = classWriter.visitMethod(access,
					INTERNAL_INIT_METHOD_NAME, desc, signature, exceptions);
			return new ModifyInitMethodAdapter(mv, originalClassName);
		}
		return null;
	}

	@Override
	public void visitEnd() {
		for (Class<?> clazz : implementClasses) {
			System.out.println("implement : " + clazz);
			try {
				ClassReader reader = new ClassReader(clazz.getName());
				ClassVisitor adapter = new ImplementClassAdapter(classWriter);
				reader.accept(adapter, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cv.visitEnd();
	}
}
