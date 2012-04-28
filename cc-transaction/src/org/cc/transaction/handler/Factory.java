/**
 * TranHandler.java 9:52:04 AM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.cc.core.asm.ClassReader;
import org.cc.core.asm.ClassVisitor;
import org.cc.core.asm.ClassWriter;
import org.cc.core.asm.app.TranHandler;
import org.cc.core.asm.handler.RuntimeLoader;
import org.cc.transaction.Transactional;

/**
 * <p>
 * </p>
 * 
 * @author dixingxing
 * @date Apr 26, 2012
 */
public class Factory {
	private static final Logger LOG = Logger.getLogger(Factory.class);
	public static final String sufix = "_EnhancedByASM";
	
	public static <T> T get(Class<T> clazz,Class<?> handlerClass)  {
		byte[] data;
		T obj = null;
		
		Class<?> superClass = clazz.getSuperclass();
//		if(superClass != null && superClass.isAnnotationPresent(Transactional.class)) {
//			try {
////				writeClass(clazz, handlerClass,1);
////				String superClassName = superClass.getName() + sufix;
////				Class<?> clazzEnhanced1 = new RuntimeLoader(TempClassHolder.get(superClassName)).defineClass(superClassName);
//			
//				
//				data = enhanceMethod(clazzEnhanced1,handlerClass,0);
//				Class<T> clazzEnhanced = new RuntimeLoader(data).defineClass(clazzEnhanced1.getName() + sufix);
//				obj = (T) clazzEnhanced.newInstance();
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new RuntimeException(e);
//			}
//			
//		} else {
			
			try {
				data = enhanceMethod(clazz,handlerClass,0);
				
				write(clazz.getSimpleName() + sufix, data);
				
				LOG.debug("loading class : " + clazz.getName() + sufix);
				Class<T> clazzEnhanced = new RuntimeLoader(data).defineClass(clazz.getName() + sufix);
//				Class<T> clazzEnhanced = (Class<T>) Class.forName(clazz.getName() + sufix);
				
				obj = (T) clazzEnhanced.newInstance();
				
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
//		}
		
		// serviceImpl
		if(superClass!=null && !superClass.isAnnotationPresent(Transactional.class)) {
			TempClassHolder.put(clazz.getName() + sufix, data);
		}
		
		

		return obj;
	}

	/**
	 * <p></p>
	 *
	 * @param <T>
	 * @param clazz
	 * @param data
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static <T> void write(String name, byte[] data)
			throws FileNotFoundException, IOException {
		File file = new File(
				"E:/java/workspace/auth/bin/com/hc360/security/service/"
						+ name +".class");
		FileOutputStream fout = new FileOutputStream(file);
		fout.write(data);
		fout.close();
	}
	
	public static byte[] getBytes(Class<?> clazz,Class<?> handlerClass)  {
		byte[] data;
		try {
			data = enhanceMethod(clazz,handlerClass,0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return data;
	}
	public static void writeClass(Class<?> clazz,Class<?> handlerClass,int is)  {
		byte[] data;
		try {
			data = enhanceMethod(clazz, handlerClass,is);

			File file = new File(
					"E:/java/workspace/auth/bin/com/hc360/security/service/"
							+ clazz.getSimpleName() +  "_EnhancedByASM.class");
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(data);
			fout.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void writeClass2(Class<?> clazz,Class<?> handlerClass,int is)  {
		byte[] data;
		try {
			data = enhanceMethod(clazz, handlerClass,is);

			File file = new File(
					"E:/java/workspace/auth/bin/com/hc360/security/service/"
							+ clazz.getSimpleName() + "_EnhancedByASM.class");
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(data);
			fout.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <p>给方法增强</p>
	 *
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	private static <T> byte[] enhanceMethod(Class<T> clazz,Class<?> handlerClass,int enhanceSuperClass) throws IOException {
		String path = clazz.getName().replace('.', '/') + ".class";
		ClassReader cr = new ClassReader(ClassLoader
				.getSystemResourceAsStream(path));
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		ClassVisitor classAdapter = new ClassAdapter(cw,handlerClass,enhanceSuperClass);
		cr.accept(classAdapter, ClassReader.EXPAND_FRAMES);
		final byte[] data = cw.toByteArray();
		
		StringBuilder sb = new StringBuilder();
		for(byte b : data) {
			sb.append((char) b);
		}
//if(clazz.getName().indexOf("RoleService") >= 0) {
//	System.out.println(sb.toString());
//}
		return data;
	}

	public static void main(String[] args) throws ClassFormatError,
			InstantiationException, IllegalAccessException, IOException {
		UserService a = Factory.get(UserService.class,TranHandler.class);
		a.run();
	}
	

}
