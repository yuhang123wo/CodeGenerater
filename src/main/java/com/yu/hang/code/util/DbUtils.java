package com.yu.hang.code.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yu.hang.code.bean.FieldInfo;
import com.yu.hang.code.bean.TableInfo;
import com.yu.hang.code.conf.Conf;

/**
 * 
 * @author Administrator
 *
 */
public class DbUtils {

	private static DbUtils dbUtils = new DbUtils();

	private static Conf conf = Conf.getInstance();

	private DbUtils() {
	}

	public static DbUtils getInstance() {
		return dbUtils;
	}

	static Logger logging = LoggerFactory.getLogger(DbUtils.class);

	public void t() throws ClassNotFoundException, SQLException {
		Connection connection = getConnection();
		DatabaseMetaData metaData = getMetaData(connection);
		List<String> tableNames = Arrays.asList("shop_info");
		List<TableInfo> tableInfos = getAllTables(metaData, tableNames);
		logging.info("tableInfos==>" + tableInfos);
	}

	public static void main(String[] args) {
		try {
			DbUtils.getInstance().t();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回一个与特定数据库的连接
	 *
	 * @throws ClassNotFoundException
	 */
	public Connection getConnection() throws ClassNotFoundException {
		Connection connection = null;
		try {
			// 加载属性文件，读取数据库连接配置信息
			Properties pro = new Properties();
			try {
				pro.load(DbUtils.class.getClassLoader().getResourceAsStream("jdbc.properties"));
			} catch (IOException e) {
				System.out.println("未找到配置文件！！！");
			}
			String driverClass = pro.getProperty("jdbc.driver");
			String url = pro.getProperty("jdbc.url");
			String user = pro.getProperty("jdbc.username");
			String password = pro.getProperty("jdbc.password");

			Properties props = new Properties();
			props.setProperty("user", user);
			props.setProperty("password", password);
			props.setProperty("remarks", "true"); // 设置可以获取remarks信息
			props.setProperty("useInformationSchema", "true");// 设置可以获取tables
																// remarks信息

			Class.forName(driverClass);
			connection = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 只做单主键代码的生成
	 *
	 * @param metaData
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public String primaryKeyColumnName(DatabaseMetaData metaData, String tableName) throws SQLException {
		String primaryKeyColumnName = null;
		ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, null, tableName);
		while (primaryKeyResultSet.next()) {
			primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
			break;
		}
		if (primaryKeyColumnName == null) {
			primaryKeyColumnName = "id";
		}
		return primaryKeyColumnName;
	}

	/**
	 * 获取需要生成代码的表信息
	 *
	 * @param metaData
	 * @param tableNames
	 * @return
	 * @throws SQLException
	 */
	public List<TableInfo> getAllTables(DatabaseMetaData metaData, List<String> tableNames) throws SQLException {
		List<TableInfo> tables = new ArrayList<TableInfo>();
		ResultSet tableRet = getTableResultSet(metaData);
		while (tableRet.next()) {
			TableInfo tableInfo = new TableInfo();
			String tableName = tableRet.getString("TABLE_NAME");// 表名
			String tableDesc = tableRet.getString("remarks");// 表注释
			for (String _tableName : tableNames) {
				if (_tableName.equals("all") || tableName.trim().equals(_tableName)) {
					System.out.println("表名:" + operateString(tableName, 18) + "  注释:" + tableDesc);
					// 字段处理
					Set<String> packages = new HashSet<String>();
					List<FieldInfo> fieldInfos = processAllColumn(metaData, tableName, packages);
					// 主键处理(主键唯一)
					String primaryKey = primaryKeyColumnName(metaData, tableName);
					String primaryKeyProperty = Underline2CamelUtils.underline2Camel2(primaryKey);
					Map<String, String> primaryKeyMap = new HashMap<String, String>();
					primaryKeyMap.put(primaryKey, primaryKeyProperty);

					// beanClass
					String beanName = getClassName(tableName);
					tableInfo.setTableName(tableName);
					tableInfo.setTableDesc(tableDesc);
					tableInfo.setFieldInfos(fieldInfos);
					tableInfo.setBeanName(beanName);
					tableInfo.setPrimaryKey(primaryKeyMap);
					tableInfo.setPackages(packages);

					tables.add(tableInfo);
				}
			}

		}
		return tables;
	}

	private List<FieldInfo> processAllColumn(DatabaseMetaData metaData, String tableName, Set<String> packages)
			throws SQLException {
		String columnName;
		String columnType;
		String remarks;
		ResultSet colRet = metaData.getColumns(null, "%", tableName, "%");
		List<FieldInfo> fieldInfos = new ArrayList<FieldInfo>();
		while (colRet.next()) {
			columnName = colRet.getString("COLUMN_NAME");
			columnType = colRet.getString("TYPE_NAME");
			remarks = colRet.getString("remarks");
			FieldInfo fieldInfo = new FieldInfo();
			fieldInfo.setColumnName(columnName);
			fieldInfo.setColumnType(columnType);
			fieldInfo.setColumnRemarks(remarks);
			fieldInfos.add(fieldInfo);
		}
		processAllColumnBean(fieldInfos, packages);
		return fieldInfos;
	}

	private void processAllColumnBean(List<FieldInfo> fieldInfos, Set<String> packages) {
		for (FieldInfo fieldInfo : fieldInfos) {
			String columnName = fieldInfo.getColumnName();// 字段名
			String columnType = fieldInfo.getColumnType();// 字段类型
			String beanName = Underline2CamelUtils.underline2Camel2(columnName);
			String beanType = getFieldType(columnType, packages, beanName);
			fieldInfo.setBeanName(beanName);
			fieldInfo.setBeanType(beanType);
		}
	}

	public boolean excludeInsertProperties(String propertyName) {
		return "id".equals(propertyName) || "createTime".equals(propertyName) || "updateTime".equals(propertyName)
				|| "delFlag".equals(propertyName);
	}

	/**
	 * str不足len长度以空格填充
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	private String operateString(String str, int len) {
		int length = str.length();
		if (length < len) {
			int tmplen = len - length;
			StringBuffer sb = new StringBuffer();
			sb.append(str);
			for (int i = 0; i < tmplen; i++) {
				sb.append(" ");
			}
			return sb.toString();
		}
		return str;
	}

	/**
	 * 获取TableResultSet
	 *
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getTableResultSet(DatabaseMetaData metaData) throws SQLException {
		// DatabaseMetaData metaData = connection.getMetaData();
		// ResultSet tableRet = metaData.getTables(null, "%", "%", new String[]
		// { "TABLE" });
		String tableName = "%";
		return getTableResultSet(metaData, tableName);

	}

	/**
	 * 获取TableResultSet
	 *
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getTableResultSet(DatabaseMetaData metaData, String tableName) throws SQLException {
		// ResultSet tableRet = metaData.getTables(null, "%", tableName, new
		// String[]{"TABLE"});
		ResultSet tableRet = metaData.getTables(null, null, null, new String[] { "TABLE" });
		return tableRet;
	}

	/**
	 * 获取DatabaseMetaData
	 *
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public DatabaseMetaData getMetaData(Connection connection) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		return metaData;
	}

	/**
	 * 如果table名是t_开头，则去掉t_,其他_变驼峰，第一个字母大写。
	 *
	 * @param tableName
	 * @return
	 */
	public static String getClassName(String tableName) {
		String res = tableName;
		// 去t_
		if (res.startsWith("t_")) {
			res = res.substring(2);
		}
		// 变驼峰
		res = Underline2CamelUtils.underline2Camel2(res);
		// 首字符大写
		res = res.substring(0, 1).toUpperCase() + res.substring(1);
		return res;
	}

	/**
	 * 设置字段类型 MySql数据类型
	 *
	 * @param columnType
	 *            列类型字符串
	 * @param packages
	 *            封装包信息
	 * @return
	 */
	public static String getFieldType(String columnType, Set<String> packages, String propertyName) {

		columnType = columnType.toLowerCase();
		if (columnType.equals("varchar") || columnType.equals("nvarchar") || columnType.equals("char")
				|| columnType.equals("text")) // ||
		// columnType.equals("tinytext")||columnType.equals("mediumtext")||columnType.equals("longtext")
		{
			return "String";
		} else if (columnType.equals("tinyblob") || columnType.equals("blob") || columnType.equals("mediumblob")
				|| columnType.equals("longblob")) {
			return "byte[]";
		} else if (columnType.equals("datetime") || columnType.equals("date") || columnType.equals("timestamp")
				|| columnType.equals("time") || columnType.equals("year")) {
			packages.add("import java.util.Date;");
			return "Date";
		} else if (columnType.equals("bit") || columnType.equals("int") || columnType.equals("tinyint")
				|| columnType.equals("smallint") || columnType.equals("tinyint unsigned")) // ||columnType.equals("bool")||columnType.equals("mediumint")
		{
			return "int";
		} else if (columnType.equals("int unsigned")) {
			return "int";
		} else if (columnType.equals("bigint unsigned")) {
			packages.add("import java.math.BigInteger;");
			return "BigInteger";
		} else if (columnType.equals("bigint")) {
			return "long";
		} else if (columnType.equals("float")) {
			return "float";
		} else if (columnType.equals("double")) {
			return "double";
		} else if (columnType.equals("decimal")) {
			packages.add("import java.math.BigDecimal;");
			return "BigDecimal";
		} else if (columnType.equals("enum")) {
			String type = getClassName(propertyName);
			packages.add(conf.getEnumPackage() + type + ";");
			return type;
		}
		return "ErrorType";
	}

}
