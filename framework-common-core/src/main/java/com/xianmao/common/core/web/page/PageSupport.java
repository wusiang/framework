package com.xianmao.common.core.web.page;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xianmao.common.core.exception.BussinessException;
import com.xianmao.common.core.utils.ServletUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageSupport {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        return pageDomain;
    }

    /**
     * 设置请求分页数据
     */
    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }

    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        startPage(false);
    }

    /**
     * 设置请求分页数据
     */
    public static void startPage(boolean isCheck) {
        PageDomain pageDomain = getPageDomain();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (null != pageNum && null != pageSize) {
            PageHelper.startPage(pageNum, pageSize);
        } else if (isCheck) {
            throw new BussinessException("分页不能为空");
        }
    }

    public static <T, R> Page<R> convert2PageDTO(List<T> origin, Function<T, R> function) {
        PageInfo<T> pageInfo = new PageInfo<>(origin);
        Page<R> page = new Page();
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
