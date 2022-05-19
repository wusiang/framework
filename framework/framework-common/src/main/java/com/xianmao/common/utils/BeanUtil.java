package com.xianmao.common.utils;

import com.xianmao.common.support.BaseBeanCopier;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @ClassName BeanUtil
 * @Description: 实体工具类
 * @Author wjh
 * @Data 2020-04-28 11:39
 * @Version 1.0
 */
public class BeanUtil {

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        return (T) BeanUtils.instantiateClass(clazz);
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
     * copy 对象属性到另一个对象，默认不使用Convert
     * <p>
     * 注意：不支持链式Bean，链式用 copyProperties
     *
     * @param source 源对象
     * @param clazz  类名
     * @param <T>    泛型标记
     * @return T
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) {
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
    public static void copyProperties(Object source, Object targetBean) {
        BaseBeanCopier copier = BaseBeanCopier
                .create(source.getClass(), targetBean.getClass(), false);
        copier.copy(source, targetBean, null);
    }


    /**
     * 属性拷贝
     *
     * @param source      list
     * @param targetClass class
     * @return 列表 the list
     */
    public static <T> List<T> copyList(List<?> source, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>();
        }
        return handleCopyList(source, targetClass);
    }

    /**
     * 属性拷贝
     *
     * @param source      数组
     * @param targetClass class
     * @return 列表 the list
     */
    public static <T> List<T> copyList(Object[] source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        List<T> target = new ArrayList<>();
        for (Object obj : source) {
            target.add(copyProperties(obj, targetClass));
        }
        return target;
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
     * 内部 属性拷贝
     *
     * @param source
     * @param targetClass
     * @return
     */
    private static <T> List<T> handleCopyList(List<?> source, Class<T> targetClass) {
        List<T> target = new ArrayList<>();
        for (Object obj : source) {
            target.add(copyProperties(obj, targetClass));
        }
        return target;
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
            PropertyDescriptor[] all = BeanUtils.getPropertyDescriptors(type);
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
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }
}
