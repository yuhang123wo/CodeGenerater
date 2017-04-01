package com.yu.hang.code.creator.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yu.hang.code.bean.Config;
import com.yu.hang.code.bean.FieldInfo;
import com.yu.hang.code.bean.TableInfo;
import com.yu.hang.code.creator.AbstractFileCreator;
import com.yu.hang.code.util.StringUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @author Administrator
 *
 */
public class BeanClassCreator extends AbstractFileCreator {
	private BeanClassCreator() {
		super();
	}

	private BeanClassCreator(Config conf) {
		super();
		init(conf);
	}

	public static synchronized BeanClassCreator getInstance(Config conf) {
		return new BeanClassCreator(conf);
	}

	@Override
	public void createFile(TableInfo tableInfo) throws IOException, TemplateException {
		String ftl = "bean.ftl";
		String fileName = tableInfo.getBeanName() + ".java";
		String selfPath = conf.getBeanPackage();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("table", tableInfo);
		root.put("conf", conf);

		Template temp = cfg.getTemplate(ftl);
		fileName = javaPath + selfPath + separator + fileName;
		createFile(fileName, root, temp);
		// printFile(root, temp);

		// 生成enum类
		List<FieldInfo> list = tableInfo.getFieldInfos();
		for (FieldInfo fi : list) {
			if (fi.getEnumValues() != null && fi.getEnumValues().size() > 0) {
				ftl = "enum.ftl";
				root.put("field", fi);
				temp = cfg.getTemplate(ftl);
				fileName = javaPath + selfPath + separator + "enums" + separator
						+ StringUtils.upperCaseFirstLetter(fi.getBeanName()) + ".java";
				createFile(fileName, root, temp);
			}
		}
	}

	@Override
	public void createFile(List<TableInfo> tableInfos, String beanName, String comments) throws IOException,
			TemplateException {
		if (tableInfos != null && tableInfos.size() > 0) {
			for (TableInfo tableInfo : tableInfos) {
				this.createFile(tableInfo);
			}
		}
	}
}
