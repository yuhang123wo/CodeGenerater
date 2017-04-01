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
public class ControllerClassCreator extends AbstractFileCreator {
	private ControllerClassCreator() {
		super();
	}

	private ControllerClassCreator(Config conf) {
		super();
		init(conf);
	}

	public static synchronized ControllerClassCreator getInstance(Config conf) {
		return new ControllerClassCreator(conf);
	}

	@Override
	public void createFile(TableInfo tableInfo) throws IOException, TemplateException {
		String ftl = "controller.ftl";
		String fileName = tableInfo.getBeanName() + "Controller.java";
		String selfPath = conf.getControllerPackage();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("table", tableInfo);
		root.put("conf", conf);
		Template temp = cfg.getTemplate(ftl);
		// fileName = javaPath + selfPath + separator + prefixName + separator +
		// fileName;
		fileName = javaPath + selfPath + separator + fileName;
		createFile(fileName, root, temp);
	}

	@Override
	public void createFile(List<TableInfo> tableInfos, String beanName, String comments) throws IOException,
			TemplateException {

	}
}
