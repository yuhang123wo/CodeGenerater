package com.yu.hang.code.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class Config {

	private String databaseName; // 数据库名字：shipping_manager
	private String basePackage; // 基础包名：com.go2.shipping.core
	private String beanPackage; // 实体目录：domain
	private String daoPackage; // dao目录：dao
	private String mapperPackage; // mapper xml目录：dao
	private String servicePackage; // service目录：service
	private String controllerPackage; // controller目录：controller
	private String entityPackage; // Entity完整路径：import
									// com.go2.shipping.common.entity.Entity;
	private String enumPackage; // enum包路径：import
								// com.go2.shipping.core.domain.dict.
	private String tables; // 需要生成的表：all & order_info,order_item,order_log
	private String needModules; // 需要生成的模块：bean,mapper,xml,service,impl
	private String path; // 代码生成路径：C:\\Users\\Administrator\\Desktop\\code

	private String driver; // 数据库驱动
	private String url; // 数据库连接
	private String username; // 数据库用户名
	private String password; // 数据库密码

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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o.getClass().equals(this.getClass()) && EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
