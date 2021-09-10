package com.xianmao.web.page;

import com.github.pagehelper.PageHelper;
import com.xianmao.exception.BizException;
import com.xianmao.utils.ServletUtil;

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


}
