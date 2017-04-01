package com.yu.hang.code.bean;

/**
 * 
 * @author Administrator
 *
 */
public class TreeVo {

	private String name;
	private TableInfo tableInfo;

	public TreeVo(String name) {
		this.name = name;
	}

	public TreeVo(String name, TableInfo tableInfo) {
		this.name = name;
		this.tableInfo = tableInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TableInfo getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}

	@Override
	public String toString() {
		return name;
	}
}
