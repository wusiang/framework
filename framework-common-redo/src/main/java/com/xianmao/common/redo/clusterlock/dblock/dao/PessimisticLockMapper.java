package com.xianmao.common.redo.clusterlock.dblock.dao;

import com.xianmao.common.redo.bean.PessimisticLockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * tbl_pessimistic_lock
 */
@Mapper
public interface PessimisticLockMapper {

    //

    /**
     * 用注解的方式，可以省去xml文件，作为组件提供对外使用时，使用者无需关注xml文件
     * xml内容如下：
     *     <select id="acquireLock" parameterType="java.util.Map" resultType="PessimisticLockDO">
     *         SELECT
     *         a.id id,
     *         a.resource resource,
     *         a.description description
     *         FROM
     *         tbl_pessimistic_lock a force index(uiq_idx_resource)
     *         WHERE 1=1
     *         <if test="resource!=null and resource!=''">
     *             AND a.resource = #{resource}
     *         </if>
     *         for update
     *     </select>
     *
     * @param resourseName
     * @return
     */
    @Select("select a.id id," +
            "a.resource resource," +
            "a.description description " +
            "from tbl_pessimistic_lock a force index(uiq_idx_resource) " +
            "WHERE 1=1 AND a.resource = #{resource} " +
            "for update")
    PessimisticLockDO acquireLock(Map<String, String> resourseName);

}
