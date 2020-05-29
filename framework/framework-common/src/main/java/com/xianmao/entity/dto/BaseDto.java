package com.xianmao.entity.dto;

import com.xianmao.entity.vo.BaseVo;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @ClassName BaseDTO
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-28 08:21
 * @Version 1.0
 */
public class BaseDto<T extends BaseVo> implements Serializable {
    private static final long serialVersionUID = 1141514595742921722L;

    /**
     * From转化为Po，进行后续业务处理
     *
     * @param clazz
     * @return
     */
    public T toVo(Class<T> clazz) {
        T t = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(this, t);
        return t;
    }
}
