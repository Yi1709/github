package com.lzw.blog.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: lzw
 * @Date: 2020/04/26/10:26
 * @Description:
 */
public class DateJsonValueProcessor implements JsonValueProcessor {

	private String format;

	public DateJsonValueProcessor(String format) {
		this.format = format;
	}

	@Override
	public Object processArrayValue(Object o, JsonConfig jsonConfig) {
		return null;
	}

	@Override
	public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
		if (o == null) {
			return "";
		}
		if (o instanceof Timestamp) {
			String str = new SimpleDateFormat(this.format).format((Timestamp) o);
			return str;
		}
		if (o instanceof Date) {
			String str = new SimpleDateFormat(this.format).format((Date) o);
			return str;
		}
		return o.toString();
	}
}
