package com.fujitsu.fnst.asm.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.cc.core.asm.ClassReader;
import org.cc.core.asm.ClassVisitor;
import org.cc.core.asm.ClassWriter;

/**
 * 
 * @author paul
 * 
 */
public class EnhanceFactory {
	private static SimpleClassLoader classLoader = new SimpleClassLoader();

	private static final String ENHANCED = "$ENHANCED";

	/**
	 * Create a subclass extends the target class.And add implements to it .
	 * 
	 * @param <T>
	 *            The target class type.
	 * @param clazz
	 *            The target class.
	 * @param implementClasses
	 *            The implementation classes.
	 * @return Enhanced class,that is extends clazz, and enhanced with
	 *         implementations.
	 * @throws EnhanceException
	 *             When error occurs during enhancing.
	 * @throws NullPointerException
	 *             If clazz or implementClasses is null.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> addImplementation(Class<T> clazz,
			Class<?>... implementClasses) throws EnhanceException {
		String enhancedClassName = clazz.getName() + ENHANCED;
		try {
			return  (Class<T>) classLoader.loadClass(enhancedClassName);
		} catch (ClassNotFoundException classNotFoundException) {
			ClassReader reader = null;
			try {
				reader = new ClassReader(clazz.getName());
			} catch (IOException ioexception) {
				throw new EnhanceException(ioexception, clazz, implementClasses);
			}
			ClassWriter writer = new ClassWriter(0);
			ClassVisitor visitor = new AddImplementClassAdapter(
					enhancedClassName, clazz, writer, implementClasses);
			reader.accept(visitor, 0);
			byte[] byteCodes = writer.toByteArray();
			Class<T> result = (Class<T>) classLoader.defineClass(
					enhancedClassName, byteCodes);
			
			StringBuilder sb = new StringBuilder();
			for(byte b : byteCodes) {
				sb.append((char) b);
			}
//			System.out.println(sb.toString());
			
			File file = new File(
					"c:/TEMP/"
							+ clazz.getSimpleName() +  "$EnhancedByASM.class");
			FileOutputStream fout;
			try {
				fout = new FileOutputStream(file);
				fout.write(byteCodes);
				fout.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
	}

	/**
	 * Create a instance of enhanced class.
	 * @param <T>
	 * @param clazz
	 * @param impls
	 * @return
	 * @throws EnhanceException
	 */
	public static <T> T newInstance(Class<T> clazz, Class<?>... impls)
			throws EnhanceException {
		Class<T> c = addImplementation(clazz, impls);
		if (c == null) {
			return null;
		}
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			throw new EnhanceException(e, clazz, impls);
		} catch (IllegalAccessException e) {
			throw new EnhanceException(e, clazz, impls);
		}
	}

}
