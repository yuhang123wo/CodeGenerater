package com.yu.hang.code.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yu.hang.code.bean.Config;
import com.yu.hang.code.bean.ConfigVo;
import com.yu.hang.code.bean.FieldInfo;
import com.yu.hang.code.bean.TableInfo;
import com.yu.hang.code.conf.ConfigLoader;

/**
 * 
 * @author Administrator
 *
 */
public class DBHelper {

	private Logger logging = LoggerFactory.getLogger(DBHelper.class);

	public static DBHelper dbHelper = null;

	private ConfigLoader configLoader = ConfigLoader.getInstance();

	private DBHelper() {
	}

	public static DBHelper getInstance() {
		if (dbHelper == null) {
			dbHelper = new DBHelper();
		}
		return dbHelper;
	}

	/**
	 * 得到数据库连接
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Connection getConnection() throws ClassNotFoundException {
		Connection connection = null;
		// 加载属性文件，读取数据库连接配置信息
		// 加载内存中另一个配置
		ConfigVo configVo = configLoader.getCurr();
		if (configVo == null) {
			logging.error("数据库配置不存在...");
			return null;
		}
		Config config = configVo.getConfig();

		Properties props = new Properties();
		props.setProperty("user", config.getUsername());
		props.setProperty("password", config.getPassword());
		props.setProperty("remarks", "true"); // 设置可以获取remarks信息
		props.setProperty("useInformationSchema", "true");// 设置可以获取tables
															// remarks信息

		logging.info("加载MySQL驱动...");
		Class.forName(config.getDriver());
		logging.info("获取数据库连接...");
		try {
			connection = DriverManager.getConnection(config.getUrl(), props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 测试数据库连接
	 * 
	 * @return true 连接成功 false 连接失败
	 * @throws ClassNotFoundException
	 */
	public boolean getConnection(String driver, String url, String username, String password) {
		boolean bool = false;
		Connection connection = null;
		// 加载属性文件，读取数据库连接配置信息
		Properties props = new Properties();
		props.setProperty("user", username);
		props.setProperty("password", password);

		try {
			logging.info("加载MySQL驱动...");
			Class.forName(driver);
			logging.info("获取数据库连接...");
			connection = DriverManager.getConnection(url, props);
			if (connection != null) {
				bool = true;
				connection.close();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			bool = false;
		}
		return bool;
	}

	/**
	 * 获取所有数据库表和他们的主键信息
	 * 
	 * @param metaData
	 * @return
	 */
	public List<TableInfo> getAllTablesWithColumns() {
		List<TableInfo> tables = CacheUtil.get(getCurrDatabaseName());
		if (tables != null) {
			return tables;
		}
		Connection connection = null;
		try {
			connection = getConnection();
			if (connection == null) {
				return new ArrayList<TableInfo>();
			}
			tables = generateTables(connection);
			getAllColumns(tables, connection);
			CacheUtil.put(getCurrDatabaseName(), tables);
			return tables;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 返回当前使用中的数据库名称
	 * 
	 * @return
	 */
	private String getCurrDatabaseName() {
		ConfigVo configVo = configLoader.getCurr();
		return configVo.getConfig().getDatabaseName();
	}

	/**
	 * 获取所有数据库表及注释
	 * 
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private List<TableInfo> generateTables(Connection connection) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		List<TableInfo> tables = new ArrayList<TableInfo>();
		ResultSet tableRet = metaData.getTables(null, "%", "%", new String[] { "TABLE" });
		while (tableRet.next()) {
			TableInfo tableInfo = new TableInfo();
			String tableName = tableRet.getString("TABLE_NAME");// 表名
			String tableDesc = tableRet.getString("remarks");// 表注释
			if (tableDesc == null) {
				tableDesc = "";
			}
			if (tableDesc.contains(";")) {
				tableDesc = tableDesc.split(";")[0];
			}
			if (tableDesc.contains("InnoDB")) {
				tableDesc = "";
			}
			System.out.println("表名:" + StringUtils.operateString(tableName, 18) + "  注释:" + tableDesc);
			// 字段处理
			Set<String> packages = new HashSet<String>();

			// beanClass
			String beanName = StringUtils.getClassName(tableName);
			tableInfo.setTableName(tableName);
			tableInfo.setTableDesc(tableDesc);
			tableInfo.setBeanName(beanName);
			tableInfo.setPackages(packages);

			tables.add(tableInfo);
		}
		return tables;
	}

	/**
	 * 获取所有数据库表和他们的主键信息
	 * 
	 * @param metaData
	 * @return
	 */
	public List<TableInfo> getAllTables() {
		Connection connection = null;
		try {
			connection = getConnection();
			if (connection == null) {
				return new ArrayList<TableInfo>();
			}
			List<TableInfo> tables = generateTables(connection);
			return tables;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 填充表对象的主键和列列表
	 * 
	 * @param tableInfos
	 */
	public void getAllColumns(List<TableInfo> tableInfos) {
		if (tableInfos == null || tableInfos.size() == 0) {
			return;
		}

		Connection connection = null;
		try {
			connection = dbHelper.getConnection();
		} catch (ClassNotFoundException e) {
			logging.error("驱动未找到...", e);
		}
		if (connection == null) {
			logging.error("获取数据库连接失败...");
			return;
		}

		try {
			DatabaseMetaData metaData = connection.getMetaData();
			for (TableInfo tableInfo : tableInfos) {
				// 主键处理(主键唯一)
				String primaryKey = primaryKeyColumnName(metaData, tableInfo.getTableName());
				String primaryKeyProperty = Underline2CamelUtils.underline2Camel2(primaryKey);
				Map<String, String> primaryKeyMap = new HashMap<String, String>();
				primaryKeyMap.put(primaryKey, primaryKeyProperty);
				// 字段处理
				Set<String> packages = new HashSet<String>();
				List<FieldInfo> fieldInfos = processAllColumn(metaData, tableInfo.getTableName(), packages);

				tableInfo.setPrimaryKey(primaryKeyMap);
				tableInfo.setFieldInfos(fieldInfos);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 填充表对象的主键和列列表，不需要自己创建连接
	 * 
	 * @param tableInfos
	 */
	public void getAllColumns(List<TableInfo> tableInfos, Connection connection) {
		if (tableInfos == null || tableInfos.size() == 0) {
			return;
		}

		try {
			DatabaseMetaData metaData = connection.getMetaData();
			for (TableInfo tableInfo : tableInfos) {
				// 主键处理(主键唯一)
				String primaryKey = primaryKeyColumnName(metaData, tableInfo.getTableName());
				String primaryKeyProperty = Underline2CamelUtils.underline2Camel2(primaryKey);
				Map<String, String> primaryKeyMap = new HashMap<String, String>();
				primaryKeyMap.put(primaryKey, primaryKeyProperty);
				// 字段处理
				Set<String> packages = new HashSet<String>();
				List<FieldInfo> fieldInfos = processAllColumn(metaData, tableInfo.getTableName(), packages);

				tableInfo.setPrimaryKey(primaryKeyMap);
				tableInfo.setFieldInfos(fieldInfos);
				tableInfo.setPackages(packages);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 只做单主键代码的生成
	 *
	 * @param metaData
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private String primaryKeyColumnName(DatabaseMetaData metaData, String tableName) throws SQLException {
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
	 * 获取表所有字段
	 * 
	 * @param metaData
	 * @param tableName
	 * @param packages
	 * @return
	 * @throws SQLException
	 */
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
			if (columnType.toLowerCase().equals("enum")) { // 枚举类型，获取所有枚举值
				Statement statement = metaData.getConnection().createStatement();
				ResultSet rs = statement.executeQuery("SHOW COLUMNS FROM " + tableName + " LIKE '" + columnName + "'");
				rs.next();
				String enums = rs.getString("Type");
				System.out.println(enums);
				Pattern p = Pattern.compile("'(.*?)'");
				Matcher m = p.matcher(enums);
				List<String> enumValues = new ArrayList<String>();
				while (m.find()) {
					enumValues.add(m.group(1));
				}
				fieldInfo.setEnumValues(enumValues);
			}
		}
		processAllColumnBean(fieldInfos, packages);
		return fieldInfos;
	}

	/**
	 * 设置列的实体名称和类型以及类型包名称
	 * 
	 * @param fieldInfos
	 * @param packages
	 */
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

	/**
	 * 设置字段类型 MySql数据类型
	 *
	 * @param columnType
	 *            列类型字符串
	 * @param packages
	 *            封装包信息
	 * @return
	 */
	private String getFieldType(String columnType, Set<String> packages, String propertyName) {

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
			String type = StringUtils.getClassName(propertyName);
			packages.add(configLoader.getCurr().getConfig().getEnumPackage() + type + ";");
			return type;
		}
		return "ErrorType";
	}

	public static void main(String[] args) {
		DBHelper dbHelper = DBHelper.getInstance();
		long start0 = System.currentTimeMillis();
		List<TableInfo> tableInfos = dbHelper.getAllTables();
		long end0 = System.currentTimeMillis();
		System.out.println("tableInfos==>1" + tableInfos);
		long start = System.currentTimeMillis();
		dbHelper.getAllColumns(tableInfos);
		long end = System.currentTimeMillis();
		System.out.println("tableInfos==>2" + tableInfos);

		System.out.println(end0 - start0);
		System.out.println(end - start);
	}
}
