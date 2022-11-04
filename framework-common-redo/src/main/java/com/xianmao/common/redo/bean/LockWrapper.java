package com.xianmao.common.redo.bean;

import org.apache.ibatis.session.SqlSession;

/**
 * 锁封装
 * author:xuyaokun_kzx
 * date:2021/7/8
 * desc:
*/
public class LockWrapper {

    /**
     * 锁资源名称
     */
    private String resourceName;

    /**
     * 锁（基于mysql事务排它锁实现，SqlSession封装了事务）
     */
    private SqlSession sqlSession;

    /**
     * 锁的可重入次数
     */
    private Long reentrantCount;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Long getReentrantCount() {
        return reentrantCount;
    }

    public void setReentrantCount(Long reentrantCount) {
        this.reentrantCount = reentrantCount;
    }

    public static LockWrapper build(String resourceName, SqlSession sqlSession){
        LockWrapper lockWrapper = new LockWrapper();
        lockWrapper.setResourceName(resourceName);
        lockWrapper.setSqlSession(sqlSession);
        lockWrapper.setReentrantCount(0L);
        return lockWrapper;
    }
}
