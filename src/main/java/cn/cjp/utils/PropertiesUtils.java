package cn.cjp.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 属性配置文件操作类
 * 
 * @author REAL
 * 
 */
public class PropertiesUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PropertiesUtils.class);

	/**
	 * 属性配置文件
	 */
	@SuppressWarnings("unused")
	private String propertiesFile = null;
	Properties p = new Properties();

	@SuppressWarnings("unused")
	private PropertiesUtils() {
	}

	/**
	 * 初始化配置文件工具类
	 * @param propertiesFile 文件名
	 */
	public PropertiesUtils(String propertiesFile) {
		FileInputStream is = null;

		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		path = path.replace("/", "\\");
		path = path.replace("file:\\", "");
		try {
			File file = new File(path , propertiesFile);
			if(!file.exists()){
				System.err.println("FileNotFoundException "+path);
			}
			is = new FileInputStream(file);
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
	 * 
	 * @param propsFile
	 */
	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	/**
	 * 获取属性值
	 * 
	 * @param key
	 * @return
	 */
	public String getPropValue(String key) {
		return p.getProperty(key);
	}
	
	/**
	 * 获取所有属性值
	 * @return Map
	 */
	public Map<String, String> getAll(){
		Map<String, String> props = new HashMap<String, String>();
		for(Object key : p.keySet()){
			props.put(key.toString(), p.get(key).toString());
		}
		if (logger.isInfoEnabled()) {
			logger.info(props);
		}
		
		return props;
	}

}
