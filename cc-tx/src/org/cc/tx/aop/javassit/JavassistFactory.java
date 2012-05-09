/**
 * EnhanceFactory.java 3:59:00 PM Apr 27, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.aop.javassit;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.cc.core.common.Exceptions;
import org.cc.tx.aop.AopFactory;
import org.cc.tx.aop.Aops;

/**
 * <p>
 * 创建事务代理对象的工厂
 * </p>
 * <li> 继承目标类，并重写所有方法（直接调用super.xxx）。
 * <li> 给所有方法的开始和结束分别增加{@link TxHandler#before(String)} 和{@link TxHandler#after(String)}调用。
 * 
 * @author dixingxing
 * @date Apr 27, 2012
 */
public class JavassistFactory extends AopFactory{
	private static final Logger LOG = Logger.getLogger(JavassistFactory.class);
	private static final String VOID = "void";
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
	protected <C> Class<C> getEnhancedClass(Class<C> cls) {
		CtClass cc = null;
		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass ccOrg = pool.get(cls.getName());
			cc = pool.makeClass(cls.getName() + SUFIX);
			// 继承目标类
			cc.setSuperclass(ccOrg);
			// 目标类的所有方法
			CtMethod[] methods = ccOrg.getMethods();

			// 重写目标类的方法。
			for (CtMethod m : methods) {
				if(!Aops.needOverride(m)) {
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
					Exceptions.uncheck(e);
				}
				cc.addMethod(m1);
				if(!isSpecial) {
					enhance(cls,m1);
				}
			}
			
			// 所有成员变量
			CtField[] fields = ccOrg.getDeclaredFields();
			for(CtField f : fields) {
				CtField f1 = new CtField(f.getType(),f.getName(),cc);
				cc.addField(f1);
			}

			byte[] bytecodes = cc.toBytecode();
			Aops.writeClazz(cls.getSimpleName() + SUFIX, bytecodes);
			return cc.toClass();
		} catch (Exception e) {
			Exceptions.uncheck(e);
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
			// 参数名
			String[] paramNames = Classes.getMethodParamNames(m);
			// 参数类型
			CtClass[] types = m.getParameterTypes();
			getMethodHead(m, sb, rtName, paramNames, types);

			// 开始进入方法
			if (rtName.equalsIgnoreCase(VOID)) {
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
			Exceptions.uncheck(e);
		}

		return sb.toString();
	}

	/**
	 * <p>构造方法头。eg. public static void main(String[] args)</p>
	 *
	 */
	private static  String getMethodHead(CtMethod m) {
		try {
			StringBuilder sb = new StringBuilder();
			String rtName = m.getReturnType().getName();

			
			// 参数名
			String[] paramNames = Classes.getMethodParamNames(m);
			// 参数类型
			CtClass[] types = m.getParameterTypes();
			getMethodHead(m, sb, rtName, paramNames, types);
			return sb.toString();
		} catch (NotFoundException e) {
			Exceptions.uncheck(e);
			return null;
		}
		
	}
	
	/**
	 * <p>构造方法头。eg. public static void main(String[] args)</p>
	 *
	 * @param m
	 * @param sb
	 * @param rtName
	 * @param paramNames
	 * @param types
	 */
	private static void getMethodHead(CtMethod m, StringBuilder sb,
			String rtName, String[] paramNames,
			CtClass[] types) {
		String rt = VOID;
		// 返回值类型是否为void
		boolean isVoid = rtName.equalsIgnoreCase(VOID);
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
	private static void enhance(Class<?> originalClass,CtMethod m1) throws CannotCompileException {
		LOG.debug("包装方法 : " + m1.getName());
		StringBuilder sb = new StringBuilder();
		CtClass[] types;
		try {
			types = m1.getParameterTypes();
			for (CtClass type : types) {
				String name  = type.getName();
				// 增加参数定义
				if(type.isArray()) {
					sb.append("[L").append(name.substring(0,name.indexOf('['))).append(";");
				} else {
					sb.append(name);
				}
				sb.append(",");
			}
			if(types.length > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
		} catch (NotFoundException e) {
			Exceptions.uncheck(e);
		}
		// className.methodName|parametertypes
		String methodInfo = originalClass.getName() + "." + m1.getName() + "|" + sb.toString();
		LOG.debug(methodInfo);
		m1.insertBefore(String.format("%s.before(\"%s\");",HANDLER_NAME ,methodInfo));
		m1.insertBefore(String.format("%s.after(\"%s\");",HANDLER_NAME, methodInfo));
	}
	
}
