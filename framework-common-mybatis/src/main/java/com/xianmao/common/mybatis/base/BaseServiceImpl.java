package com.xianmao.common.mybatis.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseDO> extends ServiceImpl<M, T> implements BaseService<T> {


}
