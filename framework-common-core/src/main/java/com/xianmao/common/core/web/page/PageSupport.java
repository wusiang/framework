package com.xianmao.common.core.web.page;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageSupport {

    public static <T, R> PageInfo<R> convertPageInfo(List<T> origin, Function<T, R> function) {
        com.github.pagehelper.PageInfo<T> pageInfo = new com.github.pagehelper.PageInfo<>(origin);
        PageInfo<R> page = new PageInfo();
        page.setTotal((int) pageInfo.getTotal());
        if (!CollectionUtils.isEmpty(origin)) {
            if (function == null) {
                page.setRows((List<R>) origin);
            } else {
                page.setRows(origin.stream().map(function::apply).collect(Collectors.toList()));
            }
        }
        return page;
    }

}
