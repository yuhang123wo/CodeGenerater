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
public class MapperXmlCreator extends AbstractFileCreator {

	private MapperXmlCreator() {
		super();
	}

	private MapperXmlCreator(Config conf) {
		super();
		init(conf);
	}

	public static synchronized MapperXmlCreator getInstance(Config conf) {
		// if (null == creator) {
		// creator = new MapperXmlCreator(conf);
		// }
		// return creator;
		return new MapperXmlCreator(conf);
	}

	@Override
	public void createFile(TableInfo tableInfo) throws IOException, TemplateException {
		String ftl = "xml.ftl";
		String fileName = tableInfo.getBeanName() + "Mapper.xml";
		String selfPath = conf.getDaoPackage();
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
		String ftl = "xmlMulti.ftl";
		String fileName = beanName + "Mapper.xml";
		String selfPath = conf.getDaoPackage();
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
