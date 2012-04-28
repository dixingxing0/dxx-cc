/**
 * EnhanceFactory.java 3:59:00 PM Apr 27, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction.javassit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.cc.core.common.Exceptions;
import org.cc.core.common.ReflectUtils;
import org.cc.transaction.handler.TempClassHolder;

/**
 * <p>
 * 创建事务代理对象的工厂
 * </p>
 * <li> 继承目标类，并重写所有方法（直接调用super.xxx）。
 * <li> 给所有方法的开始和结束分别增加{@link TranHandler#before(String)} 和{@link TranHandler#after(String)}调用。
 * 
 * @author dixingxing
 * @date Apr 27, 2012
 */
public class EnhanceFactory {
	private static final Logger LOG = Logger.getLogger(EnhanceFactory.class);
	private static String sufix = "$EnhancedByCc";

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException {
//		RoleService service = getInstance(RoleService.class);
//
//		service.query();
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(T obj) {
		Class<T> cls = (Class<T>)obj.getClass();
		Class<T> enhancedCls = null;
		if(TempClassHolder.contains(cls.getName())) {
			enhancedCls = (Class<T>) TempClassHolder.get(cls.getName());
		} else {
			enhancedCls = getEnhancedClass(cls);
			
			TempClassHolder.put(cls.getName(), enhancedCls);
		}
		
		
		
		try {
			T eObj = enhancedCls.newInstance();
			
			// copy所有成员变量的值
			Field[] fields = ReflectUtils.getVariableFields(cls);
			for(Field f : fields) {
				Object v = ReflectUtils.get(obj, f);
				LOG.debug(String.format("copy field %s : %s",f.getName(),v));
				ReflectUtils.set(eObj, f.getName(), v);
			}
			return eObj;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * 
	 * <p>
	 * 获取增加aop处理后的类型
	 * </p>
	 * <li>继承目标类型，并重写所有方法。
	 * <li>重写目标类型所有父类的所有可见方法。
	 * 
	 * @param <C>
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <C> Class<C> getEnhancedClass(Class<C> cls) {
		CtClass cc = null;
		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass ccOrg = pool.get(cls.getName());
			cc = pool.makeClass(cls.getName() + sufix);
			// 继承目标类
			cc.setSuperclass(ccOrg);
			// 目标类的所有方法
			CtMethod[] methods = ccOrg.getMethods();

			// 重写目标类的方法。
			for (CtMethod m : methods) {
				// object类本身的方法不做处理
				if (m.getDeclaringClass().getName().equals(
						Object.class.getName())) {
					continue;
				}
				
				if(Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers())
						&& m.getReturnType().getName().equals("void") && m.getName().equals("main") ) {
					continue;
				}
				String mmm = null;
				// javassist 不支持泛型，getType方法是获取泛型类型
				boolean isSpecial = m.getName().equals("getType") && cls.getGenericSuperclass() != null;
				if(isSpecial) {
					String gCls = cls.getGenericSuperclass().toString();// com.hc360.security.service.ServiceImpl<com.hc360.security.po.Role>
					String g = gCls.substring(gCls.indexOf('<') + 1);
					g = g.substring(0,g.length() - 1);
					mmm = getMethodHead(m);
					mmm = mmm + "{return \"" + g + "\";}";
				} else {
					mmm = getMethodDefineString(m);
				}
				CtMethod m1 = null;
				try{
				m1 = CtMethod.make(mmm, cc);
				}catch (Exception e) {
					e.printStackTrace();
				}
				cc.addMethod(m1);
				if(!isSpecial) {
					proxy(cls,m1);
				}
			}
			
			// 所有成员变量
			CtField[] fields = ccOrg.getFields();
			for(CtField f : fields) {
				CtField f1 = new CtField(f.getType(),f.getName(),cc);
				cc.addField(f1);
			}

			byte[] bytecodes = cc.toBytecode();
			write(cls.getSimpleName() + sufix, bytecodes);
			return cc.toClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * <p>
	 * 重写父类方法
	 * </p>
	 * <li>没有返回值 super.xxx(...);
	 * <li>有返回值 return super.xxx(...);
	 * 
	 * @param m
	 * @return
	 */
	private static String getMethodDefineString(CtMethod m) {
		StringBuilder sb = new StringBuilder();
		try {
			String rtName = m.getReturnType().getName();
			String rt = "void";
			
			// 返回值类型是否为void
			boolean isVoid = rtName.equalsIgnoreCase("void");
			// 参数名
			String[] paramNames = Classes.getMethodParamNames(m);
			// 参数类型
			CtClass[] types = m.getParameterTypes();
			getMethodHead(m, sb, rtName, rt, isVoid, paramNames, types);

			// 开始进入方法
			if (isVoid) {
				sb.append("{ ");
			} else {
				sb.append("{ return ");
			}

			// 调用父类方法 super.xxx(a,b);
			sb.append(" super.").append(m.getName()).append("(");
			// 直接使用方法的参数名
			for (String n : paramNames) {
				sb.append(n).append(",");
			}
			if (types.length > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append(");");
			// 方法结束
			sb.append("}");
			LOG.debug(String.format("覆盖父类方法:%s", sb.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * <p></p>
	 *
	 */
	private static  String getMethodHead(CtMethod m) {
		try {
			StringBuilder sb = new StringBuilder();
			String rtName = m.getReturnType().getName();

			String rt = "void";

			// 返回值类型是否为void
			boolean isVoid = rtName.equalsIgnoreCase("void");
			// 参数名
			String[] paramNames = Classes.getMethodParamNames(m);
			// 参数类型
			CtClass[] types = m.getParameterTypes();
			getMethodHead(m, sb, rtName, rt, isVoid, paramNames, types);
			return sb.toString();
		} catch (NotFoundException e) {
			Exceptions.uncheck(e);
			return null;
		}
		
	}
	
	/**
	 * <p></p>
	 *
	 * @param m
	 * @param sb
	 * @param rtName
	 * @param rt
	 * @param isVoid
	 * @param paramNames
	 * @param types
	 */
	private static void getMethodHead(CtMethod m, StringBuilder sb,
			String rtName, String rt, boolean isVoid, String[] paramNames,
			CtClass[] types) {
		if (!isVoid) {
			rt = (rtName.equals("java.lang.String") ? "String" : rtName);
		}
		// 方法定义
		sb.append(Modifier.toString(m.getModifiers()));
		sb.append(" ").append(rt).append(" ");
		sb.append(m.getName()).append("(");

		int i = 0;
		for (CtClass type : types) {
			// 增加参数定义
			sb.append(type.getName()).append(" ").append(paramNames[i])
					.append(",");
			i++;
		}
		if (types.length > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(")");
	}

	/**
	 * <p>
	 * 对方法包装
	 * </p>
	 * 
	 * @param m1
	 * @throws CannotCompileException
	 */
	private static void proxy(Class<?> originalClass,CtMethod m1) throws CannotCompileException {
		LOG.debug("包装方法 : " + m1.getName());
		StringBuilder sb = new StringBuilder();
		CtClass[] types;
		try {
			types = m1.getParameterTypes();
			for (CtClass type : types) {
				String name  = type.getName();
				// 增加参数定义
				if(type.isArray()) {
					sb.append("[L").append(name.substring(0,name.indexOf("["))).append(";");
				} else {
					sb.append(name);
				}
				sb.append(",");
			}
			if(types.length > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		String methodInfo = originalClass.getName() + "." + m1.getName() + "|" + sb.toString();
		LOG.debug(methodInfo);
		m1.insertBefore("org.cc.transaction.TranHandler.before(\"" +methodInfo+ "\");");
		m1.insertAfter("org.cc.transaction.TranHandler.after(\"" +methodInfo+ "\");");
	}

	private static <T> void write(String name, byte[] data)
			throws FileNotFoundException, IOException {
		File file = new File("C:/TEMP/" + name + ".class");
		FileOutputStream fout = new FileOutputStream(file);
		fout.write(data);
		fout.close();
	}
}
