package com.xianmao.common.redo.provider;

import com.xianmao.common.redo.provider.entity.RedoTaskDO;

import java.util.List;

public interface IRedoTaskService {

    int insert(RedoTaskDO redoTask);

    /**
     * 只查本应用对应的待补偿记录
     */
    List<RedoTaskDO> selectRedoTaskList(String applicationName);

    void delete(String redoTaskId);

    void updateExecTimes(String redoTaskId);
}
