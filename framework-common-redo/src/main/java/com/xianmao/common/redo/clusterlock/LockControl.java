package com.xianmao.common.redo.clusterlock;

public interface LockControl {

    boolean lock(String resourcName);

    boolean unlock(String resourcName);

}
