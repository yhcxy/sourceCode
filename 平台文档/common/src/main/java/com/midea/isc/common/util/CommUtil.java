package com.midea.isc.common.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FastByteArrayOutputStream;

import com.midea.isc.common.sys.Configuration;

public class CommUtil {
	private static final Logger log = LoggerFactory.getLogger(CommUtil.class);
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 从异常堆栈中提取异常信息
	 * 
	 * @param e
	 * @return 异常堆栈字符串
	 */
	public static String parseException(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String str = string2Json(sw.toString());
		return str;
	}

	/**
	 * 判断字符串是否为空，null或者length=0表示空值
	 * 
	 * @param str
	 * @return true-空字符串，false-非空字符串
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static String utf8trim(String str) {
		try {
			// 去掉首尾UTF-8空格
			String whitespace = new String(new byte[] { (byte) 0xC2, (byte) 0xA0 }, "utf-8");
			while (str.startsWith(whitespace))
				str = str.substring(1);
			while (str.endsWith(whitespace))
				str = str.substring(0, str.length() - 1);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return str.trim();
	}

	/**
	 * 空值替换
	 * 
	 * @param str1
	 * @param str2
	 * @return 如果str1为空则返回str2，否则返回str1
	 */
	public static String nvl(String str1, String str2) {
		return str1 == null || str1.length() == 0 ? str2 : str1;
	}

	/**
	 * 字符串右对齐，左边补空格
	 * 
	 * @param str
	 *            字符串
	 * @param len
	 *            总长度
	 * @return 对齐后的字符串
	 */
	public static String lpad(String str, int len) {
		return String.format("%" + len + "s", str);
	}

	/**
	 * 字符串左对齐，右边补空格
	 * 
	 * @param str
	 *            字符串
	 * @param len
	 *            总长度
	 * @return 对齐后的字符串
	 */
	public static String rpad(String str, int len) {
		return String.format("%-" + len + "s", str);
	}

	/**
	 * 数字右对齐，左边补空格
	 * 
	 * @param d
	 *            数字
	 * @param l
	 *            总长度
	 * @return 对齐后的字符串
	 */
	public static String lpad(int d, int l) {
		return String.format("%" + l + "d", d);
	}

	/**
	 * 数字左对齐，右边补空格
	 * 
	 * @param d
	 *            数字
	 * @param l
	 *            总长度
	 * @return 对齐后的字符串
	 */
	public static String rpad(int d, int l) {
		return String.format("%-" + l + "d", d);
	}

	/**
	 * 判断字符串数组是否包含指定字符串
	 * 
	 * @param a
	 *            字符串数组
	 * @param s
	 *            指定字符串
	 * @return true - 包含，false - 不包含
	 */
	public static boolean contains(String[] a, String s) {
		if (a != null) {
			for (String t : a) {
				if (t != null && t.equals(s))
					return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串数组是否包含指定字符串（忽略大小写）
	 * 
	 * @param a
	 *            字符串数组
	 * @param s
	 *            指定字符串
	 * @return true - 包含，false - 不包含
	 */
	public static boolean containsIgnoreCase(String[] a, String s) {
		if (a != null) {
			for (String t : a) {
				if (t != null && t.equalsIgnoreCase(s))
					return true;
			}
		}
		return false;
	}

	/**
	 * 字符串行列转换
	 * 
	 * @param list
	 * @param connector
	 *            拼接符
	 * @return 拼接后的字符串
	 */
	public static String concat(List<String> list, String connector) {
		String r = null;
		for (String s : list)
			r = (r == null ? s : r + connector + s);
		return r;
	}

	/**
	 * 合并List
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static <T> List<T> mergeList(List<T> left, List<T> right) {
		if (left != null) {
			if (right != null) {
				for (T e : right)
					left.add(e);
			}
			return left;
		}
		return right;
	}

	/**
	 * 拷贝对象的同名字段
	 * 
	 * @param from
	 * @param to
	 * @param exclude
	 *            排除字段，多个以","隔开，可以为null
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void copyFields(Object from, Object to, String exclude)
			throws IllegalArgumentException, IllegalAccessException {
		String[] exc = exclude == null ? "".split(",") : exclude.split(",");
		Field[] fields1 = to.getClass().getDeclaredFields();
		Field[] fields2 = from.getClass().getDeclaredFields();
		for (int i = 0; i < fields1.length; i++) {
			boolean doit = true;
			for (int k = 0; k < exc.length; k++) {
				if (exc[k].equalsIgnoreCase(fields1[i].getName())) {
					doit = false;
					break;
				}
			}
			for (int j = 0; doit && j < fields2.length; j++) {
				if (fields1[i].getName().equalsIgnoreCase(fields2[j].getName())) {

					if (fields2[j].isAccessible()) {
						if (fields1[i].isAccessible()) {
							fields1[i].set(to, fields2[j].get(from));
						} else {
							fields1[i].setAccessible(true);
							fields1[i].set(to, fields2[j].get(from));
							fields1[i].setAccessible(false);
						}
					} else {
						fields2[j].setAccessible(true);
						if (fields1[i].isAccessible()) {
							fields1[i].set(to, fields2[j].get(from));
						} else {
							fields1[i].setAccessible(true);
							fields1[i].set(to, fields2[j].get(from));
							fields1[i].setAccessible(false);
						}
						fields2[j].setAccessible(false);
					}
				}
			}
		}
	}

	/**
	 * 日期加减操作
	 * 
	 * @param d
	 *            日期
	 * @param n
	 *            偏移天数（正负数均可）
	 * @return 日期
	 */
	public static Date addDate(Date d, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}

	/**
	 * 日期加减操作
	 * 
	 * @param d
	 *            日期
	 * @param n
	 *            偏移天数（正负数均可）
	 * @return yyyy-MM-dd格式的字符串
	 */
	public static String shortDate(Date d, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, n);
		return (new SimpleDateFormat(Configuration.DateFormat)).format(cal.getTime());
	}

	/**
	 * 去掉日期的时分秒
	 * 
	 * @param d
	 *            日期
	 * @param n
	 *            偏移天数
	 * @return 去掉时分秒后的日期
	 */
	public static Date truncDate(Date d, int n) {
		Date date = null;
		try {
			date = (new SimpleDateFormat(Configuration.DateFormat)).parse(shortDate(d, n));
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 从短日期字符串转换为日期类型
	 * 
	 * @param d
	 *            Constants.DEFAULT_SHORT_DATE_FORMAT格式的日期字符串
	 * @return 转换后的日期
	 */
	public static Date fromShortDate(String d) {
		Date date = null;
		try {
			date = (new SimpleDateFormat(Configuration.DateFormat)).parse(d);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 从长日期字符串转换为日期类型
	 * 
	 * @param d
	 *            Constants.DEFAULT_FULL_DATE_FORMAT格式的日期字符串
	 * @return 转换后的日期
	 */
	public static Date fromFullDate(String d) {
		Date date = null;
		try {
			date = (new SimpleDateFormat(Configuration.DatetimeFormat)).parse(d);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 字符串数值类型转换 支持：Integer,Long,Floag,Double,Bollean,BigDecimal,Date类型
	 * 
	 * @param strValue
	 *            字符串数值
	 * @param clazz
	 *            类型
	 * @return 转换后的对象
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T fromString(String strValue, Class<T> clazz) {
		if (clazz.getName().equals(String.class.getName())) {
			return (T) strValue;
		} else if (clazz.getName().equals(java.lang.Integer.class.getName())) {
			return (T) Integer.valueOf(strValue);
		} else if (clazz.getName().equals(java.lang.Long.class.getName())) {
			return (T) Long.valueOf(strValue);
		} else if (clazz.getName().equals(java.lang.Float.class.getName())) {
			return (T) Float.valueOf(strValue);
		} else if (clazz.getName().equals(java.lang.Double.class.getName())) {
			return (T) Double.valueOf(strValue);
		} else if (clazz.getName().equals(java.lang.Boolean.class.getName())) {
			return (T) Boolean.valueOf(strValue);
		} else if (clazz.getName().equals(java.math.BigDecimal.class.getName())) {
			return (T) java.math.BigDecimal.valueOf(Double.valueOf(strValue));
		} else if (clazz.getName().equals(java.util.Date.class.getName())) {
			return (T) CommUtil.fromFullDate(strValue);
		} else {
			throw new RuntimeException("Invaid data type!");
		}
	}

	/**
	 * 日期加减操作
	 * 
	 * @param d
	 *            日期
	 * @param n
	 *            偏移天数（正负数均可）
	 * @return yyyy-MM-dd HH:mm:ss格式的字符串
	 */
	public static String fullDate(Date d, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, n);
		return (new SimpleDateFormat(Configuration.DatetimeFormat)).format(cal.getTime());
	}

	/**
	 * 获取日期的时间
	 * 
	 * @param d
	 *            日期
	 * @param n
	 *            偏移天数（正负数均可）
	 * @return HH:mm:ss格式的字符串
	 */
	public static String time(Date d, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, n);
		return (new SimpleDateFormat("HH:mm:ss")).format(cal.getTime());
	}

	/**
	 * 获取dd Mon格式的日期字符串
	 * 
	 * @param d
	 *            日期
	 * @param n
	 *            偏移天数（正负数均可）
	 * @return dd Mon格式的字符串
	 */
	@SuppressWarnings({ "deprecation" })
	public static String day(Date d, int n) {
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, n);
		return cal.getTime().getDate() + " " + months[cal.getTime().getMonth()];
	}

	/**
	 * 月份加减操作
	 * 
	 * @param d
	 *            日期
	 * @param n
	 *            加减（月）数
	 * @return 加减后的日期
	 */
	public static Date addMonth(Date d, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	/**
	 * 还原字符串中特殊字符
	 * 
	 * @param strData
	 * @return
	 */
	public static String decodeXMLString(String strData) {
		strData = replaceString(strData, "&lt;", "<");
		strData = replaceString(strData, "&gt;", ">");
		strData = replaceString(strData, "&apos;", "'");
		strData = replaceString(strData, "&quot;", "\"");
		strData = replaceString(strData, "&amp;", "&");
		return strData;
	}

	private static String replaceString(String strData, String regex, String replacement) {
		if (strData == null)
			return null;
		int index = strData.indexOf(regex);
		String strNew = "";
		if (index >= 0) {
			while (index >= 0) {
				strNew += strData.substring(0, index) + replacement;
				strData = strData.substring(index + regex.length());
				index = strData.indexOf(regex);
			}
			strNew += strData;
			return strNew;
		}
		return strData;
	}

	/**
	 * 调用restful服务
	 * 
	 * @param address
	 *            restful地址
	 * @param param
	 *            参数
	 * @return JSON字符串
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String callRestful(String address, String param) throws MalformedURLException, IOException {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		// conn.setConnectTimeout(10000);//连接超时 单位毫秒
		// conn.setReadTimeout(2000);//读取超时 单位毫秒

		conn.setDoOutput(true);// 是否输入参数

		OutputStream os = conn.getOutputStream();

		byte[] bytes = param.toString().getBytes();
		os.write(bytes);// 输入参数
		os.flush();
		os.close();

		// 读取请求返回值
		InputStream is = conn.getInputStream();

		String response = "";
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = is.read(b)) != -1) {
			String str = new String(b, 0, len, "UTF-8");
			response += str;
		}
		is.close();

		conn.disconnect();

		return response;
	}

	/**
	 * 将POJO对象转换成MAP，通过此方式可以过滤不需要输出的字段
	 * 
	 * @param obj
	 *            POJO对象
	 * @param include
	 *            包含的字段
	 * @param exclude
	 *            排除的字段
	 * @return
	 * @throws IllegalAccessException
	 */
	public static LinkedHashMap<String, Object> object2map(Object obj, String[] include, String[] exclude)
			throws IllegalAccessException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (include != null && !CommUtil.containsIgnoreCase(include, field.getName()))
				continue;

			if (exclude != null && CommUtil.containsIgnoreCase(exclude, field.getName()))
				continue;

			if (!field.isAccessible())
				field.setAccessible(true);

			map.put(field.getName(), field.get(obj));
		}
		return map;
	}

	/**
	 * 将POJO对象转换成MAP，通过此方式可以过滤不需要输出的字段
	 * 
	 * @param list
	 * @param include
	 *            包含的字段
	 * @param exclude
	 *            排除的字段
	 * @return
	 * @throws IllegalAccessException
	 */
	public static List<LinkedHashMap<String, Object>> object2map(List<? extends Object> list, String[] include,
			String[] exclude) throws IllegalAccessException {
		List<LinkedHashMap<String, Object>> data = new ArrayList<LinkedHashMap<String, Object>>();
		for (Object obj : list) {
			data.add(object2map(obj, include, exclude));
		}
		return data;
	}

	/**
	 * 拼接URL，自动从处理两端URL间的斜杠
	 * 
	 * @param webRoot
	 * @param url
	 * @return 拼接后的URL
	 */
	public static String concatUrl(String webRoot, String url) {
		return (webRoot == null ? "" : webRoot.endsWith("/") ? webRoot : webRoot + "/")
				+ (url.startsWith("/") ? url.substring(1) : url);
	}

	/**
	 * 查找Class名称
	 * 
	 * @param packageName
	 *            package名称
	 * @param dirPath
	 *            package存放路径（注意不包含package自身的路径）
	 * @return package包含的所有Class名称
	 */
	public static List<String> findClassInDir(String packageName, String dirPath) {
		String separator = "/".equals(System.getProperties().getProperty("file.separator")) ? "/" : "\\";
		File dir = new File(
				dirPath + (dirPath.endsWith(separator) ? "" : separator) + packageName.replace(".", separator));
		if (!dir.exists() || !dir.isDirectory()) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		// 在给定的目录下找到所有的文件，并且进行条件过滤
		File[] dirFiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith("class");
			}
		});

		for (File file : dirFiles) {
			if (!file.isDirectory()) {
				String className = file.getName().substring(0, file.getName().length() - 6);
				list.add(packageName + "." + className);
			}
		}
		return list;
	}

	/**
	 * 查找Class名称
	 * 
	 * @param packageName
	 *            package名称
	 * @param jarPath
	 *            jar存放路径（包含jar文件名称）
	 * @return package包含的所有Class名称
	 */
	public static List<String> findClassInJar(String packageName, String jarPath) {
		List<String> list = new ArrayList<String>();
		try (JarFile jarFile = new JarFile(jarPath)) {
			List<JarEntry> jarEntryList = new ArrayList<JarEntry>();
			Enumeration<JarEntry> ee = jarFile.entries();
			while (ee.hasMoreElements()) {
				JarEntry entry = (JarEntry) ee.nextElement();
				// 过滤我们出满足我们需求的东西
				if (entry.getName().startsWith(packageName.replace(".", "/")) && entry.getName().endsWith(".class")) {
					jarEntryList.add(entry);
				}
			}
			for (JarEntry entry : jarEntryList) {
				String className = entry.getName().replace('/', '.');
				className = className.substring(0, className.length() - 6);
				list.add(className);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return list;
	}

	/**
	 * 获取ClassPath
	 * 
	 * @param clazz
	 * @return 根据操作系统调整路径分隔符，并去掉首尾分隔符
	 */
	public static String classPath(Class<?> clazz) {
		String separator = "/".equals(System.getProperties().getProperty("file.separator")) ? "/" : "\\";
		String path = clazz.getClassLoader().getResource("").getPath();

		if (path.startsWith("/") && "\\".equals(separator))
			path = path.substring(1);

		if (path.endsWith("/"))
			path = path.substring(0, path.length() - 1);

		return path.replace("/", separator);
	}

	/**
	 * 获取应用lib路径
	 * 
	 * @param clazz
	 * @return lib路径
	 */
	public static String libPath(Class<?> clazz) {
		String separator = "/".equals(System.getProperties().getProperty("file.separator")) ? "/" : "\\";
		String path = classPath(clazz);
		return path.substring(0, path.lastIndexOf(separator) + 1) + "lib";
	}

	/**
	 * 对字符串进行编码以满足URL格式要求（尤其是URL设置非英语字符参数时需要对参数进行编码）
	 * 
	 * @param s
	 *            要编码的字符串
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String s) throws UnsupportedEncodingException {
		return CommUtil.isEmpty(s) ? "" : URLEncoder.encode(s, "utf-8");
	}

	/**
	 * Copies input stream to output stream using buffer. Streams don't have to
	 * be wrapped to buffered, since copying is already optimized.
	 */
	private static int copy(InputStream input, OutputStream output) throws IOException {
		int ioBufferSize = 16 * 1024; // 16k
		byte[] buffer = new byte[ioBufferSize];
		int count = 0;
		int read;
		while (true) {
			read = input.read(buffer, 0, ioBufferSize);
			if (read == -1) {
				break;
			}
			output.write(buffer, 0, read);
			count += read;
		}
		return count;
	}

	public static byte[] readBytes(InputStream input) throws IOException {
		FastByteArrayOutputStream output = new FastByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static String getLocalIP() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String ipAddrStr = "";
		if (addr != null) {
			byte[] ipAddr = addr.getAddress();
			for (int i = 0; i < ipAddr.length; i++) {
				if (i > 0) {
					ipAddrStr += ".";
				}
				ipAddrStr += ipAddr[i] & 0xFF;
			}
		} else {
			ipAddrStr = "127.0.0.1";
		}

		return ipAddrStr;
	}

	public static byte[] toPrimitivesByte(Byte[] oBytes) {
		byte[] bytes = new byte[oBytes.length];

		for (int i = 0; i < oBytes.length; i++) {
			bytes[i] = oBytes[i];
		}

		return bytes;
	}

	// byte[] to Byte[]
	public static Byte[] toByteObjects(byte[] bytesPrim) {
		Byte[] bytes = new Byte[bytesPrim.length];

		int i = 0;
		for (byte b : bytesPrim)
			bytes[i++] = b; // Autoboxing

		return bytes;
	}

	/**
	 * JSON字符串特殊字符处理，比如：“\A1;1300”
	 * 
	 * @param s
	 * @return String
	 */
	public static String string2Json(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取各个时区的时间
	 * 
	 * @param timeZoneOffset
	 * @return
	 * @throws ParseException
	 */
	public static Date getTimeZoneDatetime(float timeZoneOffset) throws ParseException {
		float timeZoneOff = timeZoneOffset;
		if (timeZoneOffset > 13 || timeZoneOffset < -12) {
			timeZoneOff = 0;
		}
		int newTime = (int) (timeZoneOff * 60 * 60 * 1000);
		TimeZone timeZone;
		String[] ids = TimeZone.getAvailableIDs(newTime);
		if (ids.length == 0) {
			timeZone = TimeZone.getDefault();
		} else {
			timeZone = new SimpleTimeZone(newTime, ids[0]);
		}

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(timeZone);

		SimpleDateFormat sdfReturn = new SimpleDateFormat(DATE_FORMAT);
		return sdfReturn.parse(sdf.format(new Date()));
	}
}
