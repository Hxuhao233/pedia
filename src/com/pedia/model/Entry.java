package com.pedia.model;

import java.util.Date;

public class Entry {
    private Integer eid;

    private String publisher;

    private String entryname;

    private Integer status;

    private Integer praisetimes;

    private Integer badreviewtimes;

    private Integer reporttimes;

    private Date publishtime;

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    public String getEntryname() {
        return entryname;
    }

    public void setEntryname(String entryname) {
        this.entryname = entryname == null ? null : entryname.trim();
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

    public void setReporttimes(Integer reporttimes) {
        this.reporttimes = reporttimes;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }
}