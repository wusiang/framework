package com.xianmao.common.redo.clusterlock;

public final class LockControlRegistry {

    private static LockControl defaultLockControl = null;

    public static LockControl getLockControl() {
        return defaultLockControl;
    }

    /**
     * 假如有需要定义，就进行注册
     * @param lockControl
     */
    public static synchronized void registerLockControl(LockControl lockControl) {
        defaultLockControl = lockControl;
    }

}
