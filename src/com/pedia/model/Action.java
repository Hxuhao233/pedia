package com.pedia.model;

import java.util.Date;

public class Action {
    private Integer aid;

    private Integer eid;

    private Integer uid;

    private Integer type;

    private Integer status;

    private Date actiontime;

    private String entrycontent;

    private String label1;

    private String label2;

    private String label3;

    private String label4;

    private String pictureaddr;

    private String refusereason;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getEntrycontent() {
        return entrycontent;
    }

    public void setEntrycontent(String entrycontent) {
        this.entrycontent = entrycontent == null ? null : entrycontent.trim();
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1 == null ? null : label1.trim();
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2 == null ? null : label2.trim();
    }

    public String getLabel3() {
        return label3;
    }

    public void setLabel3(String label3) {
        this.label3 = label3 == null ? null : label3.trim();
    }

    public String getLabel4() {
        return label4;
    }

    public void setLabel4(String label4) {
        this.label4 = label4 == null ? null : label4.trim();
    }

    public String getPictureaddr() {
        return pictureaddr;
    }

    public void setPictureaddr(String pictureaddr) {
        this.pictureaddr = pictureaddr == null ? null : pictureaddr.trim();
    }

    public String getRefusereason() {
        return refusereason;
    }

    public void setRefusereason(String refusereason) {
        this.refusereason = refusereason == null ? null : refusereason.trim();
    }
}