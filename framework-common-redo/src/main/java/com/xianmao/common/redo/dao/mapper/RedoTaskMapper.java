package com.xianmao.common.redo.dao.mapper;

import com.xianmao.common.redo.dao.entity.RedoTaskDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RedoTaskMapper {

    @Insert("insert into tbl_redotask(redo_task_id,application_name,max_attempts,try_forever,expired_date,req_param,create_time) " +
            "values(#{redoTaskId},#{applicationName},#{maxAttempts},#{tryForever},#{expiredDate},#{reqParam},#{createTime})")
    int insert(RedoTaskDO redoTask);

    /**
     * 只查本应用对应的待补偿记录
     * @param applicationName
     * @return
     */
    @Select("select * from tbl_redotask where application_name=#{applicationName} order by create_time desc limit 50")
    List<RedoTaskDO> selectRedoTaskList(String applicationName);

    @Delete("delete from tbl_redotask where redo_task_id=#{redoTaskId}")
    void delete(String redoTaskId);

    @Update("update tbl_redotask set exec_times=exec_times+1 where redo_task_id=#{redoTaskId}")
    void updateExecTimes(String redoTaskId);

}
