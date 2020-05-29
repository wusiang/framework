package com.xianmao.entity.form;

import com.xianmao.entity.dto.BaseDto;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @ClassName BaseParam
 * @Description: TODO
 * @Author guyi
 * @Data 2020-01-08 15:32
 * @Version 1.0
 */
public class BaseForm<T extends BaseDto> implements Serializable {

    /**
     * From转化为Po，进行后续业务处理
     *
     * @param clazz
     * @return
     */
    public T toDto(Class<T> clazz) {
        T t = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(this, t);
        return t;
    }

    /**
     * From转化为Po，进行后续业务处理
     *
     * @param id
     * @param clazz
     * @return
     */
    public T toDto(String id, Class<T> clazz) {
        T t = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(this, t);
        return t;
    }

}
