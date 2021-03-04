package com.xianmao.rest.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xianmao.exception.BizException;
import com.xianmao.rest.APIResult;
import com.xianmao.rest.domain.PageDataInfo;
import com.xianmao.rest.domain.PageDomain;
import com.xianmao.rest.domain.PageSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 控制层层通用数据处理
 */
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        this.startPage(false);
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(boolean isCheck) {
        PageDomain pageDomain = PageSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (null != pageNum && null != pageSize) {
            PageHelper.startPage(pageNum, pageSize);
        } else if (isCheck) {
            throw new BizException("分页不能为空");
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected PageDataInfo getDataTable(List<?> list) {
        return new PageDataInfo(list, new PageInfo(list).getTotal());
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected APIResult toAPIReult(int rows) {
        return rows > 0 ? APIResult.success() : APIResult.fail();
    }


}
