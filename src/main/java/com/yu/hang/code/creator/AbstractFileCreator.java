package com.yu.hang.code.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yu.hang.code.bean.Config;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 说明:
 * 
 * @author YJ
 */
public abstract class AbstractFileCreator implements FileCreator {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected static Configuration cfg; // 模版配置对象
	protected static String javabasePath = new StringBuilder().append(System.getProperty("user.dir")).append(separator)
			.append("src").append(separator).append("main").append(separator).append("java").append(separator)
			.toString();
	protected static String resourcesbasePath = new StringBuilder().append(System.getProperty("user.dir"))
			.append(separator).append("src").append(separator).append("main").append(separator).append("resources")
			.append(separator).toString();
	protected static String javaPath;
	protected static Config conf = null;

	static {
		cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setClassLoaderForTemplateLoading(AbstractFileCreator.class.getClassLoader(), "templates");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

	}

	/**
	 * @param filePath
	 *            文件路径
	 * @param root
	 *            data
	 * @param temp
	 *            模板
	 * @throws IOException
	 * @throws TemplateException
	 */
	protected void createFile(String filePath, Map<String, Object> root, Template temp) throws IOException,
			TemplateException {
		String subPath = filePath.substring(0, filePath.lastIndexOf(separator));
		File directory = new File(subPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(filePath);
		logger.info(" file path =" + filePath);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}

		OutputStream os = new FileOutputStream(file);
		Writer out = new OutputStreamWriter(os, "UTF-8");
		temp.process(root, out);
	}

	/**
	 * 输出到字符流
	 * 
	 * @param root
	 * @param temp
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String printFile(Map<String, Object> root, Template temp) throws IOException, TemplateException {
		StringWriter stringWriter = new StringWriter();
		temp.process(root, stringWriter);
		String ret = stringWriter.toString();
		return ret;
	}

	public static void init(Config _conf) {
		conf = _conf;
		javaPath = conf.getPath() + separator;
	}
}
