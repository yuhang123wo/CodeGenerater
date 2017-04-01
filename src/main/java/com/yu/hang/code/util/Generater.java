package com.yu.hang.code.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.yu.hang.code.bean.Config;
import com.yu.hang.code.bean.TableInfo;
import com.yu.hang.code.creator.FileCreator;
import com.yu.hang.code.factory.SimpleFactory;

import freemarker.template.TemplateException;

/**
 * 
 * @author Administrator
 *
 */
public class Generater {

	/**
	 * 创建单表文件
	 * 
	 * @param conf
	 * @param tableInfos
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void createFile(Config conf, List<TableInfo> tableInfos) throws IOException, TemplateException {
		List<String> modules = Arrays.asList(conf.getNeedModules().split(","));
		FileCreator creator = null;
		for (TableInfo tableInfo : tableInfos) {
			for (String module : modules) {
				creator = SimpleFactory.create(module, conf);
				creator.createFile(tableInfo);
			}
		}
	}

	/**
	 * 创建多表合并文件
	 * 
	 * @param conf
	 * @param tableInfos
	 * @param beanName
	 * @param comments
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void createMultiFile(Config conf, List<TableInfo> tableInfos, String beanName, String comments)
			throws IOException, TemplateException {
		List<String> modules = Arrays.asList(conf.getNeedModules().split(","));
		FileCreator creator = null;
		for (String module : modules) {
			creator = SimpleFactory.create(module, conf);
			creator.createFile(tableInfos, beanName, comments);
		}
	}
}
