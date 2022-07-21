package com.xianmao.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class TransferUtils {

    private static Map<String, BeanCopier> BEAN_COPIER_CACHE = new HashMap<>();

    public static <D> D newInstance(Supplier<D> supplier, Consumer<D> consumer) {
        D d = supplier.get();
        if (consumer != null) {
            consumer.accept(d);
        }
        return d;
    }

    public static <S, D> D transfer(S s, Supplier<D> supplier) {
        return transfer(s, supplier, null);
    }

    public static <S, D> D transfer(S s, Supplier<D> supplier, BiConsumer<S, D> biConsumer) {
        if (s == null) {
            return null;
        }
        D d = supplier.get();
        BeanUtils.copyProperties(s, d);
        if (biConsumer != null) {
            biConsumer.accept(s, d);
        }
        return d;
    }

    public static <S, D> List<D> transfers(Collection<S> list, Supplier<D> supplier) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper -> transfer(mapper, supplier)).collect(Collectors.toList());
    }

    public static <S, D> List<D> transfers(Collection<S> list, Supplier<D> supplier, BiConsumer<S, D> biConsumer) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper -> transfer(mapper, supplier, biConsumer)).collect(Collectors.toList());
    }

    public static void populate(Object bean, Map<String, ? extends Object> properties) {
        if ((bean == null) || (properties == null)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("BeanUtils.populate(" + bean + ", " + properties + ")");
        }

        for (final Map.Entry<String, ? extends Object> entry : properties.entrySet()) {
            final String name = entry.getKey();
            if (name == null) {
                continue;
            }
            Field field = ReflectionUtils.findField(bean.getClass(), name);
            if (field == null) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, bean, entry.getValue());
        }
    }
}
