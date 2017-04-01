package com.yu.hang.code.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.yu.hang.code.bean.Config;
import com.yu.hang.code.bean.TableInfo;
import com.yu.hang.code.creator.FileCreator;
import com.yu.hang.code.factory.SimpleFactory;
import com.yu.hang.code.util.ProgressEvent.EventType;

import freemarker.template.TemplateException;

/**
 * 
 * @author Administrator
 *
 */
public class GeneraterWithMonitor {

	private ProgressEventListener pel;

	public void addProgressEventListener(ProgressEventListener pel) {
		this.pel = pel;
	}

	private void setPel(ProgressEvent pe) {
		if (pel != null) {
			pel.onProgressEvent(pe);
		}
	}

	/**
	 * 创建单表文件
	 * 
	 * @param conf
	 * @param tableInfos
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void createFile(Config conf, List<TableInfo> tableInfos) throws IOException, TemplateException {
		List<String> modules = Arrays.asList(conf.getNeedModules().split(","));
		FileCreator creator = null;
		int index = 1;
		for (TableInfo tableInfo : tableInfos) {
			for (String module : modules) {
				setPel(new ProgressEvent(EventType.normal, index, "模块：" + module + " 表：" + tableInfo.getTableName()));
				creator = SimpleFactory.create(module, conf);
				creator.createFile(tableInfo);
			}
			index++;
		}
		setPel(new ProgressEvent(EventType.end, 0, "所有文件处理完成..."));
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
	public void createMultiFile(Config conf, List<TableInfo> tableInfos, String beanName, String comments)
			throws IOException, TemplateException {
		List<String> modules = Arrays.asList(conf.getNeedModules().split(","));
		FileCreator creator = null;
		int index = 1;
		for (String module : modules) {
			setPel(new ProgressEvent(EventType.normal, index, "模块：" + module));
			creator = SimpleFactory.create(module, conf);
			creator.createFile(tableInfos, beanName, comments);
			index++;
		}
		setPel(new ProgressEvent(EventType.end, 0, "所有模块处理完成..."));
	}
}
