package com.xianmao.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ConfigurationUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 10:40
 * @Version 1.0
 */
public class ConfigurationUtil {
    private Map<String , Object> props;

    private ConfigurationUtil() {
        String root = getClass().getClassLoader().getResource("props").getPath();
        PropertyReaderUtil pr = new PropertyReaderUtil(new File(root));
        Map<String, Object> localProps = pr.read();
        this.props = new HashMap<String , Object>();
        this.props.putAll(localProps);
    }

    private static class InstanceHolder {
        public static ConfigurationUtil instance = new ConfigurationUtil();
    }

    public static ConfigurationUtil getInstance(){
        return InstanceHolder.instance;
    }

    public static void put(Map<String , Object> props){
        getInstance().props.putAll(props);
    }

    public static void put(String key , Object val){
        getInstance().props.put(key , val);
    }

    public static String getProp(String key){
        Object obj = getInstance().props.get(key);
        return obj == null ? null : obj.toString();
    }

    public static Integer getPropInt(String key){
        String str = getProp(key);
        return str == null ? null : Integer.parseInt(str);
    }

    public static Integer getPropInt(String key , Integer defaultValue){
        String str = getProp(key);
        return str == null ? defaultValue : Integer.parseInt(str);
    }
}
