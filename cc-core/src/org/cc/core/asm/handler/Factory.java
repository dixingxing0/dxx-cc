/**
 * TranHandler.java 9:52:04 AM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.asm.handler;

import java.io.IOException;

import org.cc.core.asm.ClassReader;
import org.cc.core.asm.ClassVisitor;
import org.cc.core.asm.ClassWriter;
import org.cc.core.asm.app.TranHandler;
import org.cc.core.asm.app.UserService;

/**
 * <p>
 * </p>
 * 
 * @author dixingxing
 * @date Apr 26, 2012
 */
public class Factory {
	public static <T> T get(Class<T> clazz,Class<?> handlerClass)  {
		byte[] data;
		T obj = null;
		try {
			data = enhanceMethod(clazz,handlerClass);
			Class<T> clazzEnhanced = new RuntimeLoader(data).defineClass(clazz.getName() + "$EnhancedByASM");
			obj = (T) clazzEnhanced.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return obj;
	}

	/**
	 * <p>给方法增强</p>
	 *
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	private static <T> byte[] enhanceMethod(Class<T> clazz,Class<?> handlerClass) throws IOException {
		String path = clazz.getName().replace('.', '/') + ".class";
		ClassReader cr = new ClassReader(ClassLoader
				.getSystemResourceAsStream(path));
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		ClassVisitor classAdapter = new ClassAdapter(cw,handlerClass);
		cr.accept(classAdapter, ClassReader.EXPAND_FRAMES);
		final byte[] data = cw.toByteArray();
		
		StringBuilder sb = new StringBuilder();
		for(byte b : data) {
			sb.append((char) b);
		}
//		System.out.println(sb.toString());
		return data;
	}

	public static void main(String[] args) throws ClassFormatError,
			InstantiationException, IllegalAccessException, IOException {
		UserService a = Factory.get(UserService.class,TranHandler.class);
		a.run();
	}
	

}
