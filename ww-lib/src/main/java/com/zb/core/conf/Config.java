package com.zb.core.conf;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zb.common.utils.T2TUtil;

public class Config {

	final static Logger logger = LoggerFactory.getLogger(Config.class);
	private static String DEF_CONFIG = "spring/app.conf";
	// private static List<Line> lines = new ArrayList<Line>();
	private static final Object obj = new Object();
	private static volatile Config instance = null;
	// private static Map<String, String> configMap = new HashMap<String,
	// String>();

	private static ResourceLoader loader = ResourceLoader.getInstance();
	private static ConcurrentMap<String, String> configMap = new ConcurrentHashMap<String, String>();
	// private static final String DEFAULT_CONFIG_FILE = "test.properties";

	private static Properties prop = null;

	public String getStringByKey(String key, String propName) {
		try {
			prop = loader.getPropFromProperties(propName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		key = key.trim();
		if (!configMap.containsKey(key)) {
			if (prop.getProperty(key) != null) {
				configMap.put(key, prop.getProperty(key));
			}
		}
		return configMap.get(key);
	}
	
	public String getStringByKey(String key) {
		return getStringByKey(key,DEF_CONFIG);
	}

	private Config() {
	}

	public String get(String key) {
		if (null == key)
			throw new IllegalArgumentException("key is null!");
		return getStringByKey(key);
	}

	public String get(String key, String def) {
		if (null == key)
			throw new IllegalArgumentException("key is null!");
		return getStringByKey(key) == null ? def : getStringByKey(key);
	}

	public int getInt(String key, int def) {
		if (null == key)
			throw new IllegalArgumentException("key is null!");
		return T2TUtil.str2Int(getStringByKey(key), def);
	}

	public long getLong(String key, long def) {
		if (null == key)
			throw new IllegalArgumentException("key is null!");
		return T2TUtil.str2Long(getStringByKey(key), def);
	}

//	public void set(String key, String value) {
//		if (null == key || null == value)
//			throw new IllegalArgumentException("key or value is null!");
//		if (!configMap.containsKey(key)) {
//			Line line = new Line(key, value);
//			lines.add(line);
//		}
//		configMap.put(key, value);
//	}

//	public boolean contains(String key) {
//		return configMap.containsKey(key);
//	}

	public static Config cur() {
		if (null == instance) {
			synchronized (obj) {
				if (null == instance) {
					instance = new Config();
					//init();
				}
			}
		}
		return instance;
	}

//	public void save() {
//		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream(filePath);
//			for (Line line : lines) {
//				if (line.getType() == 0) {
//					fos.write(System.getProperty("line.separator").getBytes());
//				} else if (line.getType() == 1) {
//					fos.write((line.getValue() + System.getProperty("line.separator")).getBytes());
//				} else {
//					fos.write(
//							(line.getKey() + " = " + (configMap.get(line.getKey())) + System.getProperty("line.separator"))
//									.getBytes());
//				}
//			}
//			fos.flush();
//			fos.close();
//		} catch (Exception e) {
//			logger.error("Save Var err:", e);
//		}
//
//	}

//	private static void init() {
//		File f = new File(filePath);
//		System.out.println(f.getAbsolutePath());
//		if (!f.exists()) {
//			filePath = Config.class.getResource(filePath).getPath();
//		}
//		BufferedReader in;
//		try {
//			in = new BufferedReader(new FileReader(filePath));
//			while (in.ready()) {
//				String line = in.readLine();
//				Line e = null;
//				if (null == line) {
//					e = new Line();
//				} else {
//					line = line.trim();
//					if ("".equals(line)) {
//						e = new Line();
//					} else {
//						if ('#' == line.charAt(0)) {
//							e = new Line(line);
//						} else {
//							String arr[] = line.split("=");
//							if (arr.length == 2) {
//								String key = arr[0].trim();
//								String value = arr[1].trim();
//								e = new Line(key, value);
//								configMap.put(key, value);
//							} else {
//								e = new Line(line);
//							}
//						}
//					}
//				}
//				lines.add(e);
//			}
//			in.close();
//		} catch (Exception e) {
//			logger.error("Env Var init err:", e);
//		}
//
//	}
}
