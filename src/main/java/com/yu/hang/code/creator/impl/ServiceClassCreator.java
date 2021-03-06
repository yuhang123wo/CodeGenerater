package com.yu.hang.code.creator.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yu.hang.code.bean.Config;
import com.yu.hang.code.bean.TableInfo;
import com.yu.hang.code.creator.AbstractFileCreator;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @author Administrator
 *
 */
public class ServiceClassCreator extends AbstractFileCreator {

	private ServiceClassCreator() {
		super();
	}

	private ServiceClassCreator(Config conf) {
		super();
		init(conf);
	}

	public static synchronized ServiceClassCreator getInstance(Config conf) {
		return new ServiceClassCreator(conf);
	}

	@Override
	public void createFile(TableInfo tableInfo) throws IOException, TemplateException {
		String ftl = "service.ftl";
		String fileName = tableInfo.getBeanName() + "Service.java";
		String selfPath = conf.getServicePackage();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("table", tableInfo);
		root.put("conf", conf);
		Template temp = cfg.getTemplate(ftl);
		fileName = javaPath + selfPath + separator + fileName;
		createFile(fileName, root, temp);
	}

	@Override
	public void createFile(List<TableInfo> tableInfos, String beanName, String comments) throws IOException,
			TemplateException {
		String ftl = "serviceMulti.ftl";
		String fileName = beanName + "Service.java";
		String selfPath = conf.getServicePackage();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("tables", tableInfos);
		root.put("conf", conf);
		root.put("beanName", beanName);
		root.put("comments", comments);
		Template temp = cfg.getTemplate(ftl);
		fileName = javaPath + selfPath + separator + fileName;
		createFile(fileName, root, temp);
	}
}
