package com.xianmao.common.email.utils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FreeMarkerUtil {

	private static Log log = LogFactory.getLog(FreeMarkerUtil.class);

	public static String template2String(String templateContent, Map<String, Object> map, boolean isNeedFilter) {
		if (StringUtils.isBlank(templateContent)) {
			return null;
		}
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		Map<String, Object> newMap = new HashMap<String, Object>();

		Set<String> keySet = map.keySet();
		if (!CollectionUtils.isEmpty(keySet)) {
			for (String key : keySet) {
				Object o = map.get(key);
				if (o != null) {
					if (o instanceof String) {
						String value = o.toString();
                        value = value.trim();
						if (isNeedFilter) {
							value = StringEscapeUtils.escapeXml(value);
						}
						newMap.put(key, value);
					} else {
						newMap.put(key, o);
					}
				}
			}
		}
		Template t = null;
		try {
			t = new Template("", new StringReader(templateContent), new Configuration());
			StringWriter writer = new StringWriter();
			t.process(newMap, writer);
			return writer.toString();
		} catch (IOException e) {
			log.error("TemplateUtil -> template2String IOException.");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			log.error("TemplateUtil -> template2String TemplateException.");
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (newMap != null) {
				newMap.clear();
				newMap = null;
			}
		}
	}
}
