package com.xianmao.utils;

import com.xianmao.support.BaseBeanCopier;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @ClassName BeanUtil
 * @Description: 实体工具类
 * @Author wjh
 * @Data 2020-04-28 11:39
 * @Version 1.0
 */
public class BeanUtil extends org.springframework.beans.BeanUtils {

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiateClass(clazz);
    }

    /**
     * 实例化对象
     *
     * @param clazzStr 类名
     * @param <T>      泛型标记
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            return newInstance(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Bean的属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object getProperty(Object bean, String propertyName) {
        Assert.notNull(bean, "bean Could not null");
        return BeanMap.create(bean).get(propertyName);
    }

    /**
     * 设置Bean属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @param value        属性值
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        Assert.notNull(bean, "bean Could not null");
        BeanMap.create(bean).put(propertyName, value);
    }

    /**
     * 深复制
     * <p>
     * 注意：不支持链式Bean
     *
     * @param source 源对象
     * @param <T>    泛型标记
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(T source) {
        return (T) BeanUtil.copy(source, source.getClass());
    }

    /**
     * copy 对象属性到另一个对象，默认不使用Convert
     * <p>
     * 注意：不支持链式Bean，链式用 copyProperties
     *
     * @param source 源对象
     * @param clazz  类名
     * @param <T>    泛型标记
     * @return T
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        BaseBeanCopier copier = BaseBeanCopier.create(source.getClass(), clazz, false);

        T to = newInstance(clazz);
        copier.copy(source, to, null);
        return to;
    }

    /**
     * 拷贝对象
     * <p>
     * 注意：不支持链式Bean，链式用 copyProperties
     *
     * @param source     源对象
     * @param targetBean 需要赋值的对象
     */
    public static void copy(Object source, Object targetBean) {
        BaseBeanCopier copier = BaseBeanCopier
                .create(source.getClass(), targetBean.getClass(), false);

        copier.copy(source, targetBean, null);
    }

    /**
     * 对象转换（空元素不覆盖）
     *  
     *
     * @param source 原对象
     * @param target 目标对象
     */
    public static <T> T copyIgnoreNull(Object source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        return target;
    }

    /**
     * 对象转换
     *
     * @param source           原对象
     * @param target           目标对象
     * @param ignoreProperties 排除要copy的属性
     */
    public static <T> T copy(Object source, T target, String... ignoreProperties) {
        if (ArrayUtils.isEmpty(ignoreProperties)) {
            BeanUtils.copyProperties(source, target);
        } else {
            BeanUtils.copyProperties(source, target, ignoreProperties);
        }
        return target;
    }

    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param source the source bean
     * @param target the target bean class
     * @param <T>    泛型标记
     * @return T
     * @throws BeansException if the copying failed
     */
    public static <T> T copyProperties(Object source, Class<T> target) throws BeansException {
        T to = newInstance(target);
        BeanUtil.copyProperties(source, to);
        return to;
    }

    /**
     * 将对象装成map形式
     *
     * @param bean 源对象
     * @return {Map}
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object bean) {
        return BeanMap.create(bean);
    }

    /**
     * 将对象装成map形式
     *
     * @param bean
     * @param null2String null是否转空字符串
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> toMap(T bean, Boolean null2String) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (bean != null) {
                BeanMap beanMap = BeanMap.create(bean);
                map.putAll(beanMap);
                if (null2String) {
                    map.replaceAll((k, v) -> v == null ? "" : v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将对象装成map形式
     *
     * @param bean
     * @param null2String      null是否转空字符串
     * @param ignoreProperties 忽略字符串
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> toMap(T bean, Boolean null2String, String... ignoreProperties) {
        Map<String, Object> map = toMap(bean, null2String);
        for (String ignoreProperty : ignoreProperties) {
            map.remove(ignoreProperty);
        }
        return map;
    }

    /**
     * 将map 转为 bean
     *
     * @param beanMap   map
     * @param valueType 对象类型
     * @param <T>       泛型标记
     * @return {T}
     */
    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        T bean = BeanUtil.newInstance(valueType);
        BeanMap.create(bean).putAll(beanMap);
        return bean;
    }


    /**
     * 获取 Bean 的所有 get方法
     *
     * @param type 类
     * @return PropertyDescriptor数组
     */
    public static PropertyDescriptor[] getBeanGetters(Class type) {
        return getPropertiesHelper(type, true, false);
    }

    /**
     * 查询对象中空的元素
     *
     * @param source 查询对象
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 获取 Bean 的所有 set方法
     *
     * @param type 类
     * @return PropertyDescriptor数组
     */
    public static PropertyDescriptor[] getBeanSetters(Class type) {
        return getPropertiesHelper(type, false, true);
    }

    private static PropertyDescriptor[] getPropertiesHelper(Class type, boolean read, boolean write) {
        try {
            PropertyDescriptor[] all = BeanUtil.getPropertyDescriptors(type);
            if (read && write) {
                return all;
            } else {
                List<PropertyDescriptor> properties = new ArrayList<>(all.length);
                for (PropertyDescriptor pd : all) {
                    if (read && pd.getReadMethod() != null) {
                        properties.add(pd);
                    } else if (write && pd.getWriteMethod() != null) {
                        properties.add(pd);
                    }
                }
                return properties.toArray(new PropertyDescriptor[0]);
            }
        } catch (BeansException ex) {
            throw new CodeGenerationException(ex);
        }
    }
}
