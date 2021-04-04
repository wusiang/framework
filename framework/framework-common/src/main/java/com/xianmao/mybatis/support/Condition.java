package com.xianmao.mybatis.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianmao.rest.domain.PageSupport;
import com.xianmao.utils.Converter;

public class Condition {

    /**
     * 转化成mybatis plus中的Page
     *
     * @return IPage
     */
    public static <T> IPage<T> getPage() {
        return new Page<>(Converter.toInt(PageSupport.getPageDomain().getPageNum(), 1), Converter.toInt(PageSupport.getPageDomain().getPageSize(), 10));
    }
}