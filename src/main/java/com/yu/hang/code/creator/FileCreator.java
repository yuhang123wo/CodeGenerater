package com.yu.hang.code.creator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.yu.hang.code.bean.TableInfo;

import freemarker.template.TemplateException;

/**
 * 
 * @author Administrator
 *
 */
public interface FileCreator {
	String separator = File.separator;

	/**
	 * 单表创建
	 * 
	 * @param tableInfo
	 * @throws IOException
	 * @throws TemplateException
	 */
	void createFile(TableInfo tableInfo) throws IOException, TemplateException;

	/**
	 * 多表合并创建
	 * 
	 * @param tableInfos
	 * @param beanName
	 * @param comments
	 * @throws IOException
	 * @throws TemplateException
	 */
	void createFile(List<TableInfo> tableInfos, String beanName, String comments) throws IOException, TemplateException;
}
