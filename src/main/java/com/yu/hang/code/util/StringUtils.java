package com.yu.hang.code.util;

/**
 * 
 * @author Administrator
 *
 */
public class StringUtils {

	/**
	 * str不足len长度以空格填充
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String operateString(String str, int len) {
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
	 * 单词首字母大写
	 * 
	 * @param beanName
	 * @return
	 */
	public static String upperCaseFirstLetter(String beanName) {
		return beanName.substring(0, 1).toUpperCase() + beanName.substring(1);
	}
}
