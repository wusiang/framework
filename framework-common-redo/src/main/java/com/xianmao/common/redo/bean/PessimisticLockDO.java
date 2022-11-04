package com.xianmao.common.redo.bean;

public class PessimisticLockDO {

    private Long id; //BIGINT NOT NULL AUTO_INCREMENT,
    private String resource; //int NOT NULL COMMENT '锁定的资源，可以是方法名或者业务唯一标志',
    private String description; //varchar(1024) NOT NULL DEFAULT "" COMMENT '业务场景描述',

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
