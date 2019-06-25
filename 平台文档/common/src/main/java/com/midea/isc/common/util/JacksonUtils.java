package com.midea.isc.common.util;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.midea.isc.common.sys.IscException;

/**
 * @Description 它比json-lib的转换效率要高很多，依赖很少，社区也比较活跃
 * @author WANGXK7
 * @date 2017年6月7日 上午9:32:50
 */
public class JacksonUtils {
	private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static XmlMapper xmlMapper = new XmlMapper();
	private static String javaError = "ISC-999";

	private JacksonUtils() {
		throw new IllegalAccessError("JacksonUtils class");
	}

	/**
	 * javaBean,list,array convert to json string
	 * 
	 * @throws CrmException
	 * 
	 * @throws JsonProcessingException
	 */
	public static String obj2json(Object obj) throws IscException {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new IscException(javaError, ex.getMessage());
		}
	}

	/**
	 * json string convert to javaBean
	 * 
	 * @throws CrmException
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static <T> T json2pojo(String jsonStr, Class<T> clazz) throws IscException {
		try {
			objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			return objectMapper.readValue(jsonStr, clazz);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new IscException(javaError, ex.getMessage());
		}
	}

	/**
	 * json string convert to map
	 * 
	 * @throws CrmException
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static Map<String, Object> json2map(String jsonStr) throws IscException {
		try {
			return objectMapper.readValue(jsonStr, Map.class);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new IscException(javaError, ex.getMessage());
		}
	}

	/**
	 * json string convert to map with javaBean
	 * 
	 * @throws CrmException
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) throws IscException {
		try {
			Map<String, T> result = new HashMap<>();
			Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr, new TypeReference<Map<String, T>>() {
			});
			for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
				result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
			}
			return result;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new IscException(javaError, ex.getMessage());
		}

	}

	/**
	 * json array string convert to list with javaBean
	 * 
	 * @throws CrmException
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) throws IscException {
		try {
			List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr, new TypeReference<List<T>>() {
			});
			List<T> result = new ArrayList<>();
			for (Map<String, Object> map : list) {
				result.add(map2pojo(map, clazz));
			}
			return result;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new IscException(javaError, ex.getMessage());
		}
	}

	/**
	 * json array string convert to list with map
	 * 
	 * @throws CrmException
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static List<Map<String, Object>> json2listmap(String jsonArrayStr) throws IscException {
		try {
			return objectMapper.readValue(jsonArrayStr, new TypeReference<List<Map<String, Object>>>() {
			});
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new IscException(javaError, ex.getMessage());
		}
	}

	/**
	 * map convert to javaBean
	 */
	public static <T> T map2pojo(Map map, Class<T> clazz) {
		return objectMapper.convertValue(map, clazz);
	}

	/**
	 * json string convert to xml string
	 * 
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static String json2xml(String jsonStr) throws IscException {
		try {
			JsonNode root = objectMapper.readTree(jsonStr);
			return xmlMapper.writeValueAsString(root);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new IscException(javaError, ex.getMessage());
		}
	}

	/**
	 * xml string convert to json string
	 * 
	 * @throws IOException
	 */
	public static String xml2json(String xml) throws IOException {
		StringWriter w = new StringWriter();
		JsonParser jp = xmlMapper.getFactory().createParser(xml);
		JsonGenerator jg = objectMapper.getFactory().createGenerator(w);
		while (jp.nextToken() != null) {
			jg.copyCurrentEvent(jp);
		}
		jp.close();
		jg.close();
		return w.toString();
	}
	
	/**
	 * 将json字符串转换成java对象（支持模板类）
	 * 
	 * @param json
	 *            json字符串
	 * @param ref
	 *            示例：new TypeReference&lt;T>() {}，T可以是简单类或者模板类
	 * @return 转换后的对象
	 */
	public static <T> T json2Object(String json, TypeReference<T> ref) {
		T obj = null;
		try {
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			obj = objectMapper.readValue(json, ref);
		} catch (Exception e) {
			log.error(CommUtil.parseException(e));
		}
		return obj;
	}
}