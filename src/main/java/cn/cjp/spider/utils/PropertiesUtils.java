package cn.cjp.spider.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 属性配置文件操作类
 * @author REAL
 *
 */
public class PropertiesUtils {
	/**
	 * 属性配置文件
	 */
	@SuppressWarnings("unused")
	private String propertiesFile = null;
	Properties p = new Properties();
	
	@SuppressWarnings("unused")
	private PropertiesUtils(){}
	
	public PropertiesUtils(String propertiesFile){
		FileInputStream is = null;

		String path = this.getClass().getClass().getResource("/")
				.getPath();
		try {
			is = new FileInputStream(path + propertiesFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置属性配置文件
	 * @param propsFile
	 */
	public void setPropertiesFile(String propertiesFile){
		this.propertiesFile = propertiesFile;
	}

	/**
	 * 获取属性值
	 * @param key
	 * @return
	 */
	public String getPropValue(String key){
		return p.getProperty(key);
	}
	
}
