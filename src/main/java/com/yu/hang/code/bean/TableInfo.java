package com.yu.hang.code.bean;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 
 * @author Administrator
 *
 */
public class TableInfo extends Entity {

	private static final long serialVersionUID = -2367435516271442141L;

	/**
	 * 表名 user_info
	 */
	private String tableName;

	/**
	 * 实体名 UserInfo
	 */
	private String beanName;

	/**
	 * 表注释
	 */
	private String tableDesc;

	/**
	 * 表主键映射 {"id": "id"}
	 */
	private Map<String, String> primaryKey;
	/**
	 * 所有字段信息
	 */
	private List<FieldInfo> fieldInfos;

	/**
	 * bean类导入的包
	 */
	private Set<String> packages;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	public Map<String, String> getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Map<String, String> primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<FieldInfo> getFieldInfos() {
		return fieldInfos;
	}

	public void setFieldInfos(List<FieldInfo> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}

	public Set<String> getPackages() {
		return packages;
	}

	public void setPackages(Set<String> packages) {
		this.packages = packages;
	}
}
