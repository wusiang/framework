package com.xianmao.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName BaseServiceImpl
 * @Description: TODO
 * @Author wjh
 * @Data 2020/11/9 7:12 下午
 * @Version 1.0
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T>  {

    @Override
    public boolean deleteLogic(@NotEmpty List<Long> ids) {
        return super.removeByIds(ids);
    }
}
