package org.cc.transaction.handler;

import org.apache.log4j.Logger;
import org.cc.core.asm.AnnotationVisitor;
import org.cc.core.asm.ClassVisitor;
import org.cc.core.asm.MethodVisitor;
import org.cc.core.asm.Opcodes;
import org.cc.core.asm.handler.ChildConstructorAdapter;
import org.cc.transaction.Transactional;

public class ClassAdapter extends ClassVisitor {
	private static final Logger LOG = Logger.getLogger(ClassAdapter.class);
	private String enhancedSuperName;
	
	// 真实类的名称
	private String thisClassName;
	
	private Class<?> handlerClass;
	
	private int temp;//1,2
	
	private boolean hasTran;

	public ClassAdapter(ClassVisitor cv,Class<?> handlerClass,int enhanceSuperClass) {
		//Responsechain 的下一个 ClassVisitor，这里我们将传入 ClassWriter，
		// 负责改写后代码的输出
		super(Opcodes.ASM4,cv); 
		this.handlerClass = handlerClass;
		this.temp = enhanceSuperClass;
	} 

	public ClassAdapter(int api) {
		super(api);
		// TODO Auto-generated constructor stub
	}

	public void visit(final int version, final int access, final String name, 
			 final String signature, final String superName1, 
			 final String[] interfaces) { 
		thisClassName = name;
		String superName = superName1;
		LOG.debug(String.format("before enhance : name:%s,superNmae:%s,signature:%s", name,superName,signature));
		Class<?> cls = null;
		try {
			cls = Class.forName(name.replaceAll("/", "."));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Class<?> superClass = cls.getSuperclass();
		// 如果父类定义了事务注解 roleServiceImpl 
		if(superClass!=null && superClass.isAnnotationPresent(Transactional.class)) {
			this.enhancedSuperName = superName + Factory.sufix; // 改变父类，这里是”Account”
		} 
		// serviceImpl
		else if(superClass!=null && !superClass.isAnnotationPresent(Transactional.class)){
			this.enhancedSuperName = name;
		} 
		// roleServiceImpl_sufix
		else {
			this.enhancedSuperName = name; // 改变父类，这里是”Account”
		}
		
		// 修改当前类的superClass为superClass_EnhancedByASM
		// 即生成 RoleServiceImpl_EnhancedByASM.class
//		if(temp == 1) {
//			String enhancedName = name + sufix;  // 改变类命名
//			this.enhancedSuperName = superName +  sufix; // 改变父类，这里是”Account”
//			thisClassName = enhancedName;
////			String s =signature;
////			s = signature.replace(superName, superName + sufix);
//			super.visit(version, access, enhancedName, signature, enhancedSuperName, interfaces);
//		} 
//		
//		else if(temp == 2) {
//			String enhancedName = name + sufix;  // 改变类命名
//			this.enhancedSuperName = name; // 改变父类，这里是”Account”
//			thisClassName = name;
//			super.visit(version, access, enhancedName, signature, enhancedSuperName, interfaces);
//		} 
//		
//		else {
			
		String s = signature;
		s = s.replace(superName1, enhancedSuperName);
		LOG.debug(String.format("after enhance : name:%s,superNmae:%s ,signature:%s", name + Factory.sufix,enhancedSuperName,s));
		super.visit(version, access, name + Factory.sufix, s, enhancedSuperName, interfaces);
//		}
		
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
				LOG.debug(" init superClass : " + enhancedSuperName);
				wrappedMv = new ChildConstructorAdapter(mv, enhancedSuperName);
			} else {
				// 使用自定义 MethodVisitor，实际改写方法内容
				wrappedMv = new MethodAdapter(Opcodes.ASM4, mv,
						Opcodes.ACC_PUBLIC, name, desc, handlerClass,
						thisClassName,hasTran);
			}
		} 
        
       	 return wrappedMv; 
    }
    
    
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		hasTran = desc != null && "Lorg/cc/transaction/Transactional;".equals(desc);
		return super.visitAnnotation(desc, visible);
	}
	
	@Override
	public void visitEnd() {
//		if(implementClasses == null) {
//			super.visitEnd();
//			return;
//		}
//		for (Class<?> clazz : implementClasses) {
//			if(clazz == null) {
//				continue;
//			}
//			System.out.println("implement : " + clazz);
//			try {
//				InputStream in = new ByteArrayInputStream(TempClassHolder.get(clazz.getName()));
//				ClassReader reader = new ClassReader(in);
//				ClassVisitor adapter = new ImplementClassAdapter(cv);
//				reader.accept(adapter, 0);
//			} catch (IOException e) {
//				System.out.println(clazz.getName());
//				e.printStackTrace();
//			}
//		}
//		cv.visitEnd();
		super.visitEnd();
	}
	
}