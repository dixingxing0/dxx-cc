/**
 * CustomDateSerializer.java 11:30:27 AM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.cc.core.CcException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * jackson 反序列化日期格式定义
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public class DateSerializer extends JsonSerializer<Date> {
	@Override
	public void serialize(Date value, JsonGenerator jgen,SerializerProvider provider){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(value);
		try {
			jgen.writeString(formattedDate);
		} catch (JsonGenerationException e) {
			throw new CcException(e);
		} catch (IOException e) {
			throw new CcException(e);
		}
	}
}