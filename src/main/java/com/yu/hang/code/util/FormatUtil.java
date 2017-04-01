package com.yu.hang.code.util;

/**
 * 格式化输入工具类
 */
public final class FormatUtil {

	/**
	 * 打印输入到控制台
	 */
	public static void printJson(String jsonStr) {
		System.out.println(formatJson(jsonStr));
	}

	/**
	 * 格式化
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static String formatJson(String jsonStr) {
		if (null == jsonStr || "".equals(jsonStr))
			return "";
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		boolean isInQuot = false; // 处理字符串中的逗号不换行
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			switch (current) {
			case '{':
			case '[':
				sb.append(current);
				sb.append('\n');
				indent++;
				addIndentBlank(sb, indent);
				break;
			case '}':
			case ']':
				sb.append('\n');
				indent--;
				addIndentBlank(sb, indent);
				sb.append(current);
				break;
			case ',':
				sb.append(current);
				if (last != '\\') {
					if (!isInQuot) {						
						sb.append('\n');
						addIndentBlank(sb, indent);
					}
				}
				break;
			case '"':
				isInQuot = !isInQuot;
				sb.append(current);
				break;
			default:
				sb.append(current);
			}
		}

		return sb.toString();
	}

	/**
	 * 添加space
	 * 
	 * @param sb
	 * @param indent
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}
}