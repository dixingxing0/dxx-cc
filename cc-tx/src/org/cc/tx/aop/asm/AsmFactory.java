package org.cc.tx.aop.asm;

import java.io.IOException;

import org.cc.core.asm.ClassReader;
import org.cc.core.asm.ClassVisitor;
import org.cc.core.asm.ClassWriter;
import org.cc.tx.aop.common.AopFactory;
import org.cc.tx.aop.common.Aops;

/**
 * 
 * <p>
 * asm 代理工厂
 * </p>
 * 
 * @author dixingxing
 * @date Apr 29, 2012
 */
public class AsmFactory extends AopFactory {
	/**
	 * 
	 * <p>
	 * 返回代理类
	 * </p>
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T> Class<T> getEnhancedClass(Class<T> clazz){
		String enhancedClassName = clazz.getName() + SUFIX;
		try {
			return (Class<T>) Aops.getClassLoader().loadClass(enhancedClassName);
		} catch (ClassNotFoundException classNotFoundException) {
			ClassReader reader = null;
			try {
				reader = new ClassReader(clazz.getName());
			} catch (IOException ioexception) {
				throw new RuntimeException(ioexception);
			}
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ClassVisitor visitor = new ClassAdapter(enhancedClassName, clazz,
					writer);
			reader.accept(visitor, 0);
			byte[] byteCodes = writer.toByteArray();
			Aops.writeClazz(enhancedClassName, byteCodes);
			Class<T> result = (Class<T>) Aops.getClassLoader().defineClass(
					enhancedClassName, byteCodes);
			return result;
		}
	}

}
