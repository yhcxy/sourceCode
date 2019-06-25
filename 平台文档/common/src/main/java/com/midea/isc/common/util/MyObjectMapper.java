package com.midea.isc.common.util;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.midea.isc.common.constant.DateFormatConstants;

public class MyObjectMapper {
	private static final ObjectMapper mapper;

	public static ObjectMapper getMapper() {
		return mapper;
	}

	static {
		mapper = new ObjectMapper();

		// 通过registerModule来实现支持多种日期格式，单一日期格式可使用setDateFormat
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Date.class, new DateJsonDeserializer());
		simpleModule.addSerializer(Date.class, new JsonDateSerializer(DateFormatConstants.DEFAULT_FULL_DATE_FORMAT));
		mapper.registerModule(simpleModule);

		// 处理自定义时间格式注解
		mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
			private static final long serialVersionUID = -5165398829255619652L;

			@Override
			public Object findSerializer(Annotated a) {
				if (a instanceof AnnotatedMethod) {
					AnnotatedElement m = a.getAnnotated();
					DateTimeFormat an = m.getAnnotation(DateTimeFormat.class);
					if (an != null) {
						return new JsonDateSerializer(an.pattern());
					}
				}
				return super.findSerializer(a);
			}
		});
	}
	
	public static class DateJsonDeserializer extends JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			try {
				if (CommUtil.isEmpty(parser.getValueAsString())) {
					return null;
				} else if (parser.getValueAsString().length() > 10) {
					// 支持yyyy-MM-dd'T'HH:mm:ss格式
					String value = parser.getValueAsString().contains("T") ? parser.getValueAsString().replace("T", " ")
							: parser.getValueAsString();
					return new SimpleDateFormat(DateFormatConstants.DEFAULT_FULL_DATE_FORMAT).parse(value);
				} else {
					return new SimpleDateFormat(DateFormatConstants.DEFAULT_SHORT_DATE_FORMAT).parse(parser.getValueAsString());
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}

	}
	
	public static class JsonDateSerializer extends JsonSerializer<Date> {
		private SimpleDateFormat dateFormat;

		public JsonDateSerializer(String format) {
			dateFormat = new SimpleDateFormat(format);
		}

		@Override
		public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			String value = dateFormat.format(date);// 注意date不为null时才会调用此方法，所以不需要考虑null
			gen.writeString(value);
		}
	}
}
