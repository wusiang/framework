package com.xianmao.common.mybatis.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianmao.common.mybatis.support.PageInfo;
import com.xianmao.common.mybatis.support.PageQuery;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtils {

    /**
     * 转化成mybatis plus中的Page
     */
    public static <T> IPage<T> getPage(PageQuery pageQuery) {
        Page<T> page = new Page<>(Convert.toInt(pageQuery.getPageNo(), 1), Convert.toInt(pageQuery.getPageSize(), 10));
        if (CollectionUtil.isNotEmpty(pageQuery.getAscs())) {
            String[] ascArr = Convert.toStrArray(pageQuery.getAscs());
            for (String asc : ascArr) {
                page.addOrder(OrderItem.asc(cleanIdentifier(asc)));
            }
        }
        if (CollectionUtil.isNotEmpty(pageQuery.getDescs())) {
            String[] descArr = Convert.toStrArray(pageQuery.getDescs());
            for (String desc : descArr) {
                page.addOrder(OrderItem.desc(cleanIdentifier(desc)));
            }
        }
        return page;
    }

    public static <T, R> PageInfo<R> convert2PageInfo(IPage<T> page, Function<T, R> function) {
        PageInfo<R> pageInfo = new PageInfo<>();
        pageInfo.setTotalCount((int) page.getTotal());
        pageInfo.setPageSize((int) page.getSize());
        pageInfo.setCurrPage((int) page.getCurrent());
        pageInfo.setTotalPage((int) page.getPages());
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            if (function == null) {
                // 安全转换：当 function 为 null 时，R 必须与 T 相同
                @SuppressWarnings("unchecked")
                List<R> records = (List<R>) page.getRecords();
                pageInfo.setRows(records);
            } else {
                pageInfo.setRows(page.getRecords().stream().map(function).collect(Collectors.toList()));
            }
        }
        return pageInfo;
    }



    private static String cleanIdentifier(@Nullable String param) {
        if (param == null) {
            return null;
        }
        StringBuilder paramBuilder = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                paramBuilder.append(c);
            }
        }
        return paramBuilder.toString();
    }
}
