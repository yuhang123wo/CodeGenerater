package com.yu.hang.code.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yu.hang.code.bean.TableInfo;

/**
 * 
 * @author Administrator
 *
 */
public class CacheUtil {

	// 缓存，数据库名对应表属性
	public static Map<String, List<TableInfo>> cache = new HashMap<String, List<TableInfo>>();

	/**
	 * 放入缓存
	 * 
	 * @param key
	 * @param value
	 */
	public static void put(String key, List<TableInfo> value) {
		cache.put(key, value);
	}

	/**
	 * 从缓存取出一个数据库信息
	 * 
	 * @param key
	 * @return
	 */
	public static List<TableInfo> get(String key) {
		List<TableInfo> list = cache.get(key);
		return list;
	}

	/**
	 * 从缓存中删掉一个数据库数据
	 * 
	 * @param key
	 */
	public static void remove(String key) {
		cache.remove(key);
	}

	/**
	 * 清空缓存
	 */
	public static void clear() {
		cache.clear();
	}
}
