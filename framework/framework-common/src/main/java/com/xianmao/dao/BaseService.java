package com.xianmao.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>  The Model Class 这里是泛型不是Model类
 * @param <ID> The Primary Key Class 如果是无主键，则可以用Model来跳过，如果是多主键则是Key类
 * @ClassName BaseService
 * @Description: 公用service接口
 * @Author guyi
 * @Data 2019-08-14 10:21
 * @Version 1.0
 */
public interface BaseService<T, ID extends Serializable> {

    int insertSelective(T record);

    int updateByPrimaryKeySelective(T record);

    /**
     * 删除Service层物理删除接口，防止上层调用，实际业务一般不允许物理删除，而采用逻辑删除update
     * int deleteByPrimaryKey(ID id);
     */

    int insert(T record);

    T selectByPrimaryKey(ID id);

    List<T> selectAll();

    int updateByPrimaryKey(T record);

    int insertBatch(List<T> records);

    int deleteBatch(List<ID> keys);

    List<T> selectByPrimaryKeys(List<ID> keys);

}
