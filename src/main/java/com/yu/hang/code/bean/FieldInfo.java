package com.yu.hang.code.bean;

import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public class FieldInfo extends Entity {

	private static final long serialVersionUID = 2752607643008955419L;

	private String beanName; // 属性名
	private String beanType; // 属性类型
	private String columnName; // 字段名
	private String columnType; // 字段类型
	private String columnRemarks; // 字段注释
	private List<String> enumValues; // 类型为enum时保存所有枚举值

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanType() {
		return beanType;
	}

	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnRemarks() {
		return columnRemarks;
	}

	public void setColumnRemarks(String columnRemarks) {
		this.columnRemarks = columnRemarks;
	}

	public List<String> getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(List<String> enumValues) {
		this.enumValues = enumValues;
	}

}
