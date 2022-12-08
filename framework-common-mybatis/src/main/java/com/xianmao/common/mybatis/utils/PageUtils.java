package com.xianmao.common.mybatis.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianmao.common.mybatis.support.PageQuery;
import org.springframework.lang.Nullable;

public class PageUtils {

    /**
     * 转化成mybatis plus中的Page
     *
     * @param pageQuery 查询条件
     * @return IPage
     */
    public static <T> IPage<T> getPage(PageQuery pageQuery) {
        Page<T> page = new Page<>(Convert.toInt(pageQuery.getPageNo(), 1), Convert.toInt(pageQuery.getPageSize(), 10));
        if (StrUtil.isNotBlank(pageQuery.getAscs())) {
            String[] ascArr = Convert.toStrArray(pageQuery.getAscs());
            for (String asc : ascArr) {
                page.addOrder(OrderItem.asc(cleanIdentifier(asc)));
            }
        }
        if (StrUtil.isNotBlank(pageQuery.getDescs())) {
            String[] descArr = Convert.toStrArray(pageQuery.getDescs());
            for (String desc : descArr) {
                page.addOrder(OrderItem.desc(cleanIdentifier(desc)));
            }
        }
        return page;
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
