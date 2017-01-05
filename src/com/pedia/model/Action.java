package com.pedia.model;

import java.util.Date;

public class Action {
    private Integer aid;

    private Integer eid;

    private Integer uid;

    private Integer neweid;

    private Integer status;

    private Date actiontime;

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getNeweid() {
        return neweid;
    }

    public void setNeweid(Integer neweid) {
        this.neweid = neweid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getActiontime() {
        return actiontime;
    }

    public void setActiontime(Date actiontime) {
        this.actiontime = actiontime;
    }
}