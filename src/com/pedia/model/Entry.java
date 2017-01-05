package com.pedia.model;

import java.util.Date;

public class Entry {
    private Integer eid;

    private Integer uid;

    private String entryname;

    private String entrycontent;

    private Integer status;

    private Integer praisetimes;

    private Integer badreviewtimes;

    private Integer reporttimes;

    private String pictureaddr;

    private Date publishtime;

    private String refusereason;

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

    public String getEntryname() {
        return entryname;
    }

    public void setEntryname(String entryname) {
        this.entryname = entryname == null ? null : entryname.trim();
    }

    public String getEntrycontent() {
        return entrycontent;
    }

    public void setEntrycontent(String entrycontent) {
        this.entrycontent = entrycontent == null ? null : entrycontent.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPraisetimes() {
        return praisetimes;
    }

    public void setPraisetimes(Integer praisetimes) {
        this.praisetimes = praisetimes;
    }

    public Integer getBadreviewtimes() {
        return badreviewtimes;
    }

    public void setBadreviewtimes(Integer badreviewtimes) {
        this.badreviewtimes = badreviewtimes;
    }

    public Integer getReporttimes() {
        return reporttimes;
    }

    public void setResporttimes(Integer resporttimes) {
        this.reporttimes = resporttimes;
    }

    public String getPictureaddr() {
        return pictureaddr;
    }

    public void setPictureaddr(String pictureaddr) {
        this.pictureaddr = pictureaddr == null ? null : pictureaddr.trim();
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public String getRefusereason() {
        return refusereason;
    }

    public void setRefusereason(String refusereason) {
        this.refusereason = refusereason == null ? null : refusereason.trim();
    }
}