package com.xianmao.common.redo.clusterlock;

import com.xianmao.common.redo.clusterlock.dblock.DBClusterLockHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLockControl implements LockControl{

    @Autowired
    private DBClusterLockHandler dbClusterLockHandler;

    @Override
    public boolean lock(String resourcName) {
        return dbClusterLockHandler.lock(resourcName);
    }

    @Override
    public boolean unlock(String resourcName) {
        return dbClusterLockHandler.unlock(resourcName);
    }

}
