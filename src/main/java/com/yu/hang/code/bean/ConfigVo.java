package com.yu.hang.code.bean;

/**
 * @类说明：
 * 
 * @author YANGJIE
 * @version 1.0
 * @创建时间：2016年8月12日 上午11:28:07
 */
public class ConfigVo {

	private String name; // 配置名称
	private int isDefault; // 是否默认连接
	private Config config; // 配置详情

	/**
	 * 1 默认连接
	 */
	public static final int IS_DEFAULT_YES = 1;
	/**
	 * 0 非默认连接
	 */
	public static final int IS_DEFAULT_NO = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return name;
	}
}
