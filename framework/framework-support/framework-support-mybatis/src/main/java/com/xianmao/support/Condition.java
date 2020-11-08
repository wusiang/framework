package com.xianmao.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianmao.utils.BeanUtil;
import com.xianmao.utils.Converter;

import java.util.Map;

/**
 * @ClassName Condition
 * @Description: 分页工具
 * @Author wjh
 * @Data 2020/11/9 12:21 上午
 * @Version 1.0
 */
public class Condition {

    /**
     * 转化成mybatis plus中的Page
     *
     * @param query 查询条件
     * @return IPage
     */
    public static <T> IPage<T> getPage(Query query) {
        Page<T> page = new Page<>(Converter.toInt(query.getCurrent(), 1), Converter.toInt(query.getSize(), 10));
        page.setAsc(Converter.toStringArray(SqlKeyword.filter(query.getAscs())));
        page.setDesc(Converter.toStringArray(SqlKeyword.filter(query.getDescs())));
        return page;
    }

    /**
     * 获取mybatis plus中的QueryWrapper
     *
     * @param entity 实体
     * @param <T>    类型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        return new QueryWrapper<>(entity);
    }


    /**
     * 获取mybatis plus中的QueryWrapper
     *
     * @param query   查询条件
     * @param exclude 排除的查询条件
     * @param clazz   实体类
     * @param <T>     类型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Map<String, Object> exclude, Class<T> clazz) {
        exclude.forEach((k, v) -> query.remove(k));
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(BeanUtil.newInstance(clazz));
        SqlKeyword.buildCondition(query, qw);
        return qw;
    }
}
