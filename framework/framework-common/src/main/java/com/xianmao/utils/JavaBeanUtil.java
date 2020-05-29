package com.xianmao.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @ClassName JavaBeanUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:53
 * @Version 1.0
 */
public class JavaBeanUtil {

    private JavaBeanUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static <T> T mapToClass(Map map, Class<T> clazz) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mapToModel(map, obj);
        return obj;
    }

    public static Object mapToModel(Map map, Object obj) {

        if (map == null || map.isEmpty()) {
            return null;
        }
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod != null) {
                try {
                    Object value = map.get(descriptor.getName());
                    Type[] parameterTypes = writeMethod.getGenericParameterTypes();
                    String typeName = parameterTypes[0].getTypeName();
                    if (BigDecimal.class.getName().equals(typeName)) {
                        value = ArithUtil.toBigDecimal(value);
                    }
                    writeMethod.invoke(obj, value);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return obj;
    }
}
