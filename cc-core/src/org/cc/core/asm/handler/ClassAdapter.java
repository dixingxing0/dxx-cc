package org.cc.core.asm.handler;

import org.cc.core.asm.AnnotationVisitor;
import org.cc.core.asm.ClassReader;
import org.cc.core.asm.ClassVisitor;
import org.cc.core.asm.MethodVisitor;
import org.cc.core.asm.Opcodes;

public class ClassAdapter extends ClassVisitor {
	private String enhancedSuperName;
	
	private Class<?> handlerClass;

	public ClassAdapter(ClassVisitor cv,Class<?> handlerClass) {
		//Responsechain 的下一个 ClassVisitor，这里我们将传入 ClassWriter，
		// 负责改写后代码的输出
		super(Opcodes.ASM4,cv); 
		this.handlerClass = handlerClass;
	} 

	public void visit(final int version, final int access, final String name, 
			 final String signature, final String superName, 
			 final String[] interfaces) { 
		 String enhancedName = name + "$EnhancedByASM";  // 改变类命名
		 this.enhancedSuperName = name; // 改变父类，这里是”Account”
		 super.visit(version, access, enhancedName, signature, enhancedSuperName, interfaces);
	} 
	
    // 重写 visitMethod，访问到 "operation" 方法时，
    // 给出自定义 MethodVisitor，实际改写方法内容
    public MethodVisitor visitMethod(final int access, final String name, 
        final String desc, final String signature, final String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
				exceptions);
    	
		MethodVisitor wrappedMv = mv;
    	
		if (mv != null) {
			if (name.equals("<init>")) {
				wrappedMv = new ChildConstructorAdapter(mv, enhancedSuperName);
			} else {
				// 使用自定义 MethodVisitor，实际改写方法内容
				wrappedMv = new MethodAdapter(Opcodes.ASM4, mv,
						Opcodes.ACC_PUBLIC, name, desc, handlerClass,
						enhancedSuperName);
			}
		} 
        
        
       	 return wrappedMv; 
    }

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		System.out.println(desc);
		return super.visitAnnotation(desc, visible);
	} 
	
	
}