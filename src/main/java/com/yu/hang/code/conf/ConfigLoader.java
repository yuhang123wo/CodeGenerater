package com.yu.hang.code.conf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.yu.hang.code.bean.Config;
import com.yu.hang.code.bean.ConfigVo;
import com.yu.hang.code.util.FormatUtil;
import com.yu.hang.code.util.JsonMapper;

public class ConfigLoader {
	private static ConfigLoader configLoader = null;
	private final String FILE_NAME = "conf/conf.json";
	private List<ConfigVo> configVos = new ArrayList<ConfigVo>(); // 所有配置
	private ConfigVo curr = null; // 当前使用的配置

	private ConfigLoader() {
		String jsonString = extractJSON();
		if (StringUtils.isBlank(jsonString)) {
			configVos = new ArrayList<ConfigVo>();
			return;
		}
		configVos = JsonMapper.nonDefaultMapper().fromJson(jsonString,
				JsonMapper.nonEmptyMapper().contructCollectionType(List.class, ConfigVo.class));

		if (configVos != null && configVos.size() > 1) { // 排序，默认连接排前面
			Collections.sort(configVos, new Comparator<ConfigVo>() {
				@Override
				public int compare(ConfigVo o1, ConfigVo o2) {
					return o2.getIsDefault() - o1.getIsDefault();
				};
			});
			curr = configVos.get(0);
		}
	}

	public static void main(String[] args) {
		ConfigLoader configLoader = ConfigLoader.getInstance();
		System.out.println(configLoader.getConfigVos());
		System.out.println(configLoader.getConfigVos().get(0).getConfig());
		System.out.println(configLoader.getCurr());
		if (configLoader.getConfigVos().size() == 0) {
			List<ConfigVo> confs = configLoader.getConfigVos();
			ConfigVo cv = new ConfigVo();
			Config config = new Config();
			config.setDatabaseName("shipping_manage");
			cv.setName("partime");
			cv.setConfig(config);
			confs.add(cv);
			configLoader.saveToJson();
		}
	}

	public static synchronized ConfigLoader getInstance() {
		if (configLoader == null)
			configLoader = new ConfigLoader();
		return configLoader;
	}

	@SuppressWarnings("resource")
	private String extractJSON() {
		File file = new File(FILE_NAME);
		String jsonString = "";
		String temp;
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while ((temp = bufferedReader.readLine()) != null)
				jsonString += temp;
		} catch (FileNotFoundException e) {
			System.out.println("\n[x] Configuration file not found");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("\n[x] An error occurred when trying to read the configuration file");
			System.exit(0);
		}
		return jsonString;
	}

	/**
	 * 保存配置到文件
	 */
	public void saveToJson() {
		String conf = JsonMapper.nonEmptyMapper().toJson(configVos);
		conf = FormatUtil.formatJson(conf);
		File file = new File(FILE_NAME);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			// 写文件
			fileWriter.write(conf);
			// 关闭
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ConfigVo> getConfigVos() {
		return configVos;
	}

	public void setConfigVos(List<ConfigVo> configVos) {
		this.configVos = configVos;
	}

	public ConfigVo getCurr() {
		return curr;
	}

	public void setCurr(ConfigVo curr) {
		this.curr = curr;
	}
}
