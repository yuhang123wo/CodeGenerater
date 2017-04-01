package com.yu.hang.code.conf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.yu.hang.code.bean.Entity;

/**
 * 配置文件
 * 
 * @author YJ
 */
public class Conf extends Entity {

	private static final long serialVersionUID = -5835319735617556873L;
	private static List<String> allModules = Arrays.asList("bean", "controller", "service", "impl", "mapper", "xml");
	private String databaseName;
	private String basePackage;
	private String beanPackage;
	private String daoPackage;
	private String mapperPackage;
	private String servicePackage;
	private String controllerPackage;
	private String entityPackage; // Entity完整路径
	private String enumPackage; // enum包路径
	private String tables;
	private String needModules;
	private String path;

	private static Conf conf = null;

	private Conf() {
	}

	public static Conf getInstance() {
		if (conf == null) {
			conf = new Conf().init();
		}
		return conf;
	}

	public static List<String> getAllModules() {
		return allModules;
	}

	public static void setAllModules(List<String> allModules) {
		Conf.allModules = allModules;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getBeanPackage() {
		return beanPackage;
	}

	public void setBeanPackage(String beanPackage) {
		this.beanPackage = beanPackage;
	}

	public String getDaoPackage() {
		return daoPackage;
	}

	public void setDaoPackage(String daoPackage) {
		this.daoPackage = daoPackage;
	}

	public String getMapperPackage() {
		return mapperPackage;
	}

	public void setMapperPackage(String mapperPackage) {
		this.mapperPackage = mapperPackage;
	}

	public String getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public String getControllerPackage() {
		return controllerPackage;
	}

	public void setControllerPackage(String controllerPackage) {
		this.controllerPackage = controllerPackage;
	}

	public String getEntityPackage() {
		return entityPackage;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	public String getEnumPackage() {
		return enumPackage;
	}

	public void setEnumPackage(String enumPackage) {
		this.enumPackage = enumPackage;
	}

	public String getTables() {
		return tables;
	}

	public void setTables(String tables) {
		this.tables = tables;
	}

	public String getNeedModules() {
		return needModules;
	}

	public void setNeedModules(String needModules) {
		this.needModules = needModules;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getModules() {
		List<String> modules = new ArrayList<>();
		String needModules = getNeedModules();
		if (null == needModules || needModules.equals("all")) {
			modules = allModules;
		} else {
			modules = Arrays.asList(needModules.split(","));
		}
		return modules;
	}

	/**
	 * 初始化配置文件
	 */
	public Conf init() {
		Properties pro = new Properties();
		try {
			pro.load(Conf.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			System.out.println("未找到配置文件！！！");
		}

		String tables = pro.getProperty("tables");
		String needModules = pro.getProperty("needModules");
		String path = pro.getProperty("path");
		this.setDatabaseName(pro.getProperty("databaseName"));
		this.setBasePackage(pro.getProperty("basePackage"));
		this.setBeanPackage(pro.getProperty("beanPackage"));
		this.setDaoPackage(pro.getProperty("daoPackage"));
		this.setMapperPackage(pro.getProperty("mapperPackage"));
		this.setServicePackage(pro.getProperty("servicePackage"));
		this.setControllerPackage(pro.getProperty("controllerPackage"));
		this.setEntityPackage(pro.getProperty("entityPackage"));
		this.setEnumPackage(pro.getProperty("enumPackage"));
		this.setTables(tables);
		this.setNeedModules(needModules);
		this.setPath(path);

		return this;
	}

	public static void main(String[] args) {
		Conf conf = Conf.getInstance();
		System.out.println(conf);
	}
}
