/**
 * ClasspathScanHandler.java 11:29:42 AM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package web.context;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * ɨ��ָ�����(jar���µ�class�ļ� <br>
 * <a href="http://sjsky.iteye.com">http://sjsky.iteye.com</a>
 * 
 * @author michael
 */
public class ClassPathScanUtils {

	/**
	 * LOG
	 */
	private static final Logger LOG = Logger
			.getLogger(ClassPathScanUtils.class);

	/**
	 * �Ƿ��ų��ڲ��� true->�� false->��
	 */
	private boolean excludeInner = true;
	/**
	 * ���˹���������� true��>�����Ϲ���� false->�ų��Ϲ����
	 */
	private boolean checkInOrEx = true;

	/**
	 * ���˹����б� �����null���߿գ���ȫ����ϲ�����
	 */
	private List<String> classFilters = null;

	/**
	 * �޲ι�����Ĭ�����ų��ڲ��ࡢ�������Ϲ���
	 */
	public ClassPathScanUtils() {
	}

	/**
	 * excludeInner:�Ƿ��ų��ڲ��� true->�� false->��<br>
	 * checkInOrEx�����˹���������� true��>�����Ϲ���� false->�ų��Ϲ����<br>
	 * classFilters���Զ�����˹��������null���߿գ���ȫ����ϲ�����
	 * 
	 * @param excludeInner
	 * @param checkInOrEx
	 * @param classFilters
	 */
	public ClassPathScanUtils(Boolean excludeInner, Boolean checkInOrEx,
			List<String> classFilters) {
		this.excludeInner = excludeInner;
		this.checkInOrEx = checkInOrEx;
		this.classFilters = classFilters;

	}

	/**
	 * ɨ���
	 * 
	 * @param basePackage
	 *            ���
	 * @param recursive
	 *            �Ƿ�ݹ������Ӱ�
	 * @return Set
	 */
	public Set<Class<?>> getPackageAllClasses(String basePackage,
			boolean recursive) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String packageName = basePackage;
		if (packageName.endsWith(".")) {
			packageName = packageName
					.substring(0, packageName.lastIndexOf('.'));
		}
		String package2Path = packageName.replace('.', '/');

		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(
					package2Path);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					LOG.info("ɨ��file���͵�class�ļ�....");
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					doScanPackageClassesByFile(classes, packageName, filePath,
							recursive);
				} else if ("jar".equals(protocol)) {
					LOG.info("ɨ��jar�ļ��е���....");
					doScanPackageClassesByJar(packageName, url, recursive,
							classes);
				}
			}
		} catch (IOException e) {
			LOG.error("IOException error:", e);
		}

		return classes;
	}

	/**
	 * ��jar�ķ�ʽɨ����µ�����Class�ļ�<br>
	 * 
	 * @param basePackage
	 *            eg��michael.utils.
	 * @param url
	 * @param recursive
	 * @param classes
	 */
	private void doScanPackageClassesByJar(String basePackage, URL url,
			final boolean recursive, Set<Class<?>> classes) {
		String packageName = basePackage;
		String package2Path = packageName.replace('.', '/');
		JarFile jar;
		try {
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (!name.startsWith(package2Path) || entry.isDirectory()) {
					continue;
				}

				// �ж��Ƿ�ݹ������Ӱ�
				if (!recursive
						&& name.lastIndexOf('/') != package2Path.length()) {
					continue;
				}
				// �ж��Ƿ���� inner class
				if (this.excludeInner && name.indexOf('$') != -1) {
					LOG.info("exclude inner class with name:" + name);
					continue;
				}
				String classSimpleName = name
						.substring(name.lastIndexOf('/') + 1);
				// �ж��Ƿ��Ϲ������
				if (this.filterClassName(classSimpleName)) {
					String className = name.replace('/', '.');
					className = className.substring(0, className.length() - 6);
					try {
						classes.add(Thread.currentThread()
								.getContextClassLoader().loadClass(className));
					} catch (ClassNotFoundException e) {
						LOG.error("Class.forName error:", e);
					}
				}
			}
		} catch (IOException e) {
			LOG.error("IOException error:", e);
		}
	}

	/**
	 * ���ļ��ķ�ʽɨ����µ�����Class�ļ�
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	private void doScanPackageClassesByFile(Set<Class<?>> classes,
			String packageName, String packagePath, boolean recursive) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		final boolean fileRecursive = recursive;
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// �Զ����ļ����˹���
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return fileRecursive;
				}
				String filename = file.getName();
				if (excludeInner && filename.indexOf('$') != -1) {
					LOG.info("exclude inner class with name:" + filename);
					return false;
				}
				return filterClassName(filename);
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				doScanPackageClassesByFile(classes, packageName + "."
						+ file.getName(), file.getAbsolutePath(), recursive);
			} else {
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader()
							.loadClass(packageName + '.' + className));

				} catch (ClassNotFoundException e) {
					LOG.error("IOException error:", e);
				}
			}
		}
	}

	/**
	 * ��ݹ��˹����ж�����
	 * 
	 * @param className
	 * @return
	 */
	private boolean filterClassName(String className) {
		if (!className.endsWith(".class")) {
			return false;
		}
		if (null == this.classFilters || this.classFilters.isEmpty()) {
			return true;
		}
		String tmpName = className.substring(0, className.length() - 6);
		boolean flag = false;
		for (String str : classFilters) {
			String tmpreg = "^" + str.replace("*", ".*") + "$";
			Pattern p = Pattern.compile(tmpreg);
			if (p.matcher(tmpName).find()) {
				flag = true;
				break;
			}
		}
		return (checkInOrEx && flag) || (!checkInOrEx && !flag);
	}

	/**
	 * @return the excludeInner
	 */
	public boolean isExcludeInner() {
		return excludeInner;
	}

	/**
	 * @return the checkInOrEx
	 */
	public boolean isCheckInOrEx() {
		return checkInOrEx;
	}

	/**
	 * @return the classFilters
	 */
	public List<String> getClassFilters() {
		return classFilters;
	}

	/**
	 * @param pExcludeInner
	 *            the excludeInner to set
	 */
	public void setExcludeInner(boolean pExcludeInner) {
		excludeInner = pExcludeInner;
	}

	/**
	 * @param pCheckInOrEx
	 *            the checkInOrEx to set
	 */
	public void setCheckInOrEx(boolean pCheckInOrEx) {
		checkInOrEx = pCheckInOrEx;
	}

	/**
	 * @param pClassFilters
	 *            the classFilters to set
	 */
	public void setClassFilters(List<String> pClassFilters) {
		classFilters = pClassFilters;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// �Զ�����˹���
		List<String> classFilters = new ArrayList<String>();
		// classFilters.add("File*");

		// ����һ��ɨ�账�����ų��ڲ��� ɨ�����������
		ClassPathScanUtils handler = new ClassPathScanUtils(true, true,
				classFilters);

		Set<Class<?>> calssList = handler.getPackageAllClasses("web", true);
		for (Class<?> cla : calssList) {
			System.out.println(cla.getName());
		}
	}
}
