package com.xianmao.common.redo.clusterlock.dblock;

import com.xianmao.common.redo.clusterlock.ClusterLockHandler;

public class DBOptimismClusterLockHandler implements ClusterLockHandler {

    /**
     * 乐观锁-上锁
     * @param resourseName
     */
    @Override
    public boolean lock(String resourseName) {
        /*
            执行update，同时修改状态为占有和版本号加一，假如成功，说明抢到锁，且该锁是占有状态！
            其他线程无法再更新这把锁！等执行任务完毕之后，再把这个锁更新成 可用状态
            但万一修改成占有状态后宕机了，该锁将不可再被抢占，会造成问题。
            所以需要设置一个超时时间，配合定时任务重新设置为可用状态，让所有线程继续抢锁。
            所以业务在使用时，必须评估一个合理的超时时间

            抢锁的时候要适当sleep，否则会造成CPU繁忙
         */
        return true;
    }

    /**
     * 乐观锁--解锁
     * @param resourceName
     * @return
     */
    @Override
    public boolean unlock(String resourceName) {
        return false;
    }
}
