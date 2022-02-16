package com.xianmao.common.web.page;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xianmao.common.exception.BizException;
import com.xianmao.common.utils.BeanUtil;
import com.xianmao.common.utils.ServletUtil;

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
        pageDomain.setPageNum(ServletUtil.getParameterToInt(PAGE_NUM, 1));
        pageDomain.setPageSize(ServletUtil.getParameterToInt(PAGE_SIZE, 10));
        pageDomain.setIsAsc(ServletUtil.getParameter(IS_ASC));
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
            throw new BizException("分页不能为空");
        }
    }

    public static <R, T> Page<R> transferListClass(List<T> origin, Function<T, R> mapper) {
        if (CollectionUtil.isEmpty(origin)) {
            return new Page<>();
        }
        List<R> collect = origin.stream().map(mapper)
                .collect(Collectors.toList());
        return transferPage(collect, null);
    }


    public static <R, T> Page<R> transferPage(List<T> origin, Class<R> rClass) {
        if (CollectionUtil.isEmpty(origin)) {
            return new Page();
        }
        PageInfo<T> pageInfo = new PageInfo<>(origin);
        Page<R> zPage = new Page<>();
        zPage.setPageNum(pageInfo.getPageNum());
        zPage.setPageSize(pageInfo.getPageSize());
        zPage.setTotal(pageInfo.getTotal());
        zPage.setRows(BeanUtil.copyList(origin, rClass));
        return zPage;
    }

}
