package com.xianmao.base;

import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName BaseService
 * @Description: TODO
 * @Author wjh
 * @Data 2020/11/9 12:28 上午
 * @Version 1.0
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 逻辑删除
     *
     * @param ids id集合(逗号分隔)
     * @return boolean
     */
    boolean deleteLogic(@NotEmpty List<Long> ids);
}
