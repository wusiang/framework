package com.xianmao.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class TransferUtils {

    // 使用 ConcurrentHashMap 保证线程安全
    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    public static <D> D newInstance(Supplier<D> supplier, Consumer<D> consumer) {
        D d = supplier.get();
        if (consumer != null) {
            consumer.accept(d);
        }
        return d;
    }

    private static <S, D> BeanCopier getBeanCopier(Class<S> sourceClass, Class<D> targetClass) {
        String key = sourceClass.getName() + "-" + targetClass.getName();
        return BEAN_COPIER_CACHE.computeIfAbsent(key, k -> BeanCopier.create(sourceClass, targetClass, false));
    }

    public static <S, D> D transfer(S s, Supplier<D> supplier) {
        return transfer(s, supplier, null);
    }

    public static <S, D> D transfer(S s, Supplier<D> supplier, BiConsumer<S, D> biConsumer) {
        if (s == null) {
            return null;
        }
        D d = supplier.get();
        // 使用 BeanCopier 提高性能
        BeanCopier copier = getBeanCopier(s.getClass(), d.getClass());
        copier.copy(s, d, null);
        if (biConsumer != null) {
            biConsumer.accept(s, d);
        }
        return d;
    }

    public static <S, D> List<D> transfers(Collection<S> list, Supplier<D> supplier) {
        return transferCollection(list, supplier, null);
    }

    public static <S, D> List<D> transfers(Collection<S> list, Supplier<D> supplier, BiConsumer<S, D> biConsumer) {
        return transferCollection(list, supplier, biConsumer);
    }

    public static void populate(Object bean, Map<String, ? extends Object> properties) {
        if (bean == null || properties == null || properties.isEmpty()) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("BeanUtils.populate(" + bean + ", " + properties + ")");
        }
        for (Map.Entry<String, ? extends Object> entry : properties.entrySet()) {
            String name = entry.getKey();
            if (name == null) {
                continue;
            }
            Field field = ReflectionUtils.findField(bean.getClass(), name);
            if (field == null) {
                log.debug("Field {} not found in class {}", name, bean.getClass().getName());
                continue;
            }
            try {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, bean, entry.getValue());
            } catch (Exception e) {
                log.error("Failed to set field {} in class {}", name, bean.getClass().getName(), e);
            }
        }
    }

    private static <S, D> List<D> transferCollection(Collection<S> list, Supplier<D> supplier, BiConsumer<S, D> biConsumer) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(mapper -> transfer(mapper, supplier, biConsumer))
                .collect(Collectors.toList());
    }
}