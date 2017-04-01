package com.yu.hang.code.generate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yu.hang.code.bean.Config;
import com.yu.hang.code.bean.ConfigVo;
import com.yu.hang.code.bean.TableInfo;
import com.yu.hang.code.conf.ConfigLoader;
import com.yu.hang.code.util.DbUtils;
import com.yu.hang.code.util.Generater;
import com.yu.hang.code.util.JsonMapper;

import freemarker.template.TemplateException;

public class App {
	private static Logger logging = LoggerFactory.getLogger(App.class);

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void doCreate() throws SQLException, ClassNotFoundException, IOException, TemplateException {
		// 基础信息
		ConfigVo configVo = ConfigLoader.getInstance().getCurr();
		Config conf = configVo.getConfig();
		// 表集合
		List<TableInfo> tableInfos = getTableInfos(conf);
		logging.info("tableInfos ==>" + JsonMapper.nonDefaultMapper().toJson(tableInfos));
		// 生成单表文件
		Generater.createFile(conf, tableInfos);
	}

	/**
	 * 需要生成代码的表
	 *
	 * @param conf
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private List<TableInfo> getTableInfos(Config conf) throws ClassNotFoundException, SQLException {
		Connection connection = DbUtils.getInstance().getConnection();
		DatabaseMetaData metaData = DbUtils.getInstance().getMetaData(connection);
		List<String> tableNames = Arrays.asList(conf.getTables().split(","));
		return DbUtils.getInstance().getAllTables(metaData, tableNames);
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, TemplateException, IOException {
		new App().doCreate();
	}
}
