/**
 * CustomDateSerializer.java 11:30:27 AM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.cc.core.CcException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * jackson 反序列化日期格式定义
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public class DateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt){
		Date d = null;
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(jp.getText());
		} catch (JsonParseException e) {
			throw new CcException(e);
		} catch (ParseException e) {
			throw new CcException(e);
		} catch (IOException e) {
			throw new CcException(e);
		}
		return d;
	}

}