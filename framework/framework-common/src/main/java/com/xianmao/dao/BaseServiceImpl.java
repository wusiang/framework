package com.xianmao.dao;

import org.springframework.beans.factory.annotation.Autowired;

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
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    @Autowired
    protected BaseDao<T, ID> dao;

    @Override
    public int insert(T record) {
        return dao.insert(record);
    }

    @Override
    public int insertSelective(T record) {
        return dao.insertSelective(record);
    }

    @Override
    public T selectByPrimaryKey(ID id) {
        return dao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return dao.updateByPrimaryKeySelective(record);
    }

    /**
     * 删除Service层物理删除接口，防止上层调用，实际业务一般不允许物理删除，而采用逻辑删除update
     * @Override
     *     public int deleteByPrimaryKey(ID id) {
     *         return dao.deleteByPrimaryKey(id);
     *     }
     */

    @Override
    public int updateByPrimaryKey(T record) {
        return dao.updateByPrimaryKey(record);
    }

    @Override
    public int insertBatch(List<T> records) {
        return dao.insertBatch(records);
    }

    @Override
    public int deleteBatch(List<ID> keys) {
        return dao.deleteBatch(keys);
    }

    @Override
    public List<T> selectByPrimaryKeys(List<ID> keys) {
        return dao.selectByPrimaryKeys(keys);
    }

    @Override
    public List<T> selectAll() {
        return dao.selectAll();
    }
}
