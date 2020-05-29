package com.xianmao.rest;

import com.xianmao.obj.ObjectUtil;
import com.xianmao.utils.BeanUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ResponsePage
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:27
 * @Version 1.0
 */
public class Page<T> implements Serializable {

    public static final String DATA = "data";
    public static final String PAGE = "page";

    /**
     * 当前页码
     */
    private int page;

    /**
     * 单页面记录总数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int pageTotal;

    /**
     * 总记录数
     */
    private int recordTotal;

    private T data;

    /**
     * 扩展字段
     */
    private Map<String, Object> ext;

    public static <T> Page<List<T>> valueOfDAL(Map<String, Object> resultMap, Class<T> clazz) {
        Page<List<T>> p = new Page<>();
        if (null != resultMap) {
            Map<String, Object> pageMap = (Map<String, Object>) resultMap.get(PAGE);
            if (null != pageMap) {
                Object index = pageMap.get("page");
                if (ObjectUtil.isNotEmpty(index)) {
                    p.setPage(Integer.valueOf(String.valueOf(index)));
                }
                Object size = pageMap.get("pageSize");
                if (ObjectUtil.isNotEmpty(size)) {
                    p.setPageSize(Integer.valueOf(String.valueOf(size)));
                }
                Object pTotal = pageMap.get("pageCount");
                if (ObjectUtil.isNotEmpty(pTotal)) {
                    p.setPageTotal(Integer.valueOf(String.valueOf(pTotal)));
                }
                Object rTotal = pageMap.get("recordTotal");
                if (ObjectUtil.isNotEmpty(rTotal)) {
                    p.setRecordTotal(Integer.valueOf(String.valueOf(rTotal)));
                }
            }
            List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get(DATA);
            if (null != list) {
                p.setData(mapListToObjList(list, clazz));
            }
        }
        return p;
    }

    private static <T> List<T> mapListToObjList(List<Map<String, Object>> resultList, Class<T> beanClass) {
        if (null != resultList && resultList.size() > 0) {
            List<T> rtList = new ArrayList<T>();
            for (Map<String, Object> itemMap : resultList) {
                rtList.add(BeanUtil.toBean(itemMap, beanClass));
            }
            return rtList;
        }
        return null;
    }

    public Map<String, Object> getPageInfo() {
        Map<String, Object> page = new HashMap<>();
        page.put("page", this.getPage());
        page.put("pageTotal", this.getPageTotal());
        page.put("pageSize", this.getPageSize());
        page.put("recordTotal", this.getRecordTotal());
        return page;
    }

    public static String getDATA() {
        return DATA;
    }

    public static String getPAGE() {
        return PAGE;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }
}
