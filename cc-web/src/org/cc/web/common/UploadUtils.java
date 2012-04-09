package org.cc.web.common;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 上传文件的工具类
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
public final class UploadUtils {
	private static final Logger LOG = Logger.getLogger(UploadUtils.class);
	
	private static final String UPLOAD_PATH = "file";
	private static final long UPLOAD_MAX_SIZE = 2000000L;

	private UploadUtils() {}
	
	/**
	 * 判断是否是上传文件
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isMultipart(HttpServletRequest request) {
		return FileUpload.isMultipartContent(new ServletRequestContext(request));
	}

	/**
	 * 上传文件
	 * 
	 * @param request
	 * @return 文件名（ 多个用逗号分隔）
	 */
	@SuppressWarnings( { "unchecked", "deprecation" })
	public static String upload(HttpServletRequest request) {
		if (!isMultipart(request)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		
		try {
			String basePath = request.getSession().getServletContext().getRealPath("/") + UPLOAD_PATH;

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setRepository(new File(basePath));
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(UPLOAD_MAX_SIZE);
			
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem fileItem : items) {
				if (fileItem.isFormField()) {
					continue;
				}

				if (fileItem.getName() != null && fileItem.getSize() != 0) {
					String fileName = getFileName(basePath,
							getExtention(fileItem));
					File newFile = new File(basePath + "/" + fileName);
					fileItem.write(newFile);
					sb.append(fileName).append(",");

				} else {
					LOG.debug("文件没有选择 或 文件内容为空");
				}
			}
		} catch (Exception e) {
			LOG.error("上传文件出错", e);
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			LOG.debug("已上传文件:" + sb.toString());
		}
		return sb.toString();
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileItem
	 * @return .jpg
	 */
	private static String getExtention(FileItem fileItem) {
		if (fileItem.getName() != null) {
			return fileItem.getName().substring(fileItem.getName().indexOf("."));
		}
		return null;
	}

	/**
	 * 获取文件名（根据当前时间）
	 * 
	 * @param basePath
	 * @param ext
	 * @return
	 */
	private static String getFileName(String basePath, String ext) {
		String s = String.valueOf(Calendar.getInstance().getTimeInMillis());
		String name = s + (StringUtils.isNotBlank(ext) ? ext : "");
		File newFile = new File(basePath + name);
		if (newFile.exists()) {
			return getFileName(basePath, ext);
		}
		return name;
	}

}