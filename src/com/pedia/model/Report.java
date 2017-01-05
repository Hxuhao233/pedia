package com.pedia.model;

import java.util.Date;

public class Report {
    private Integer rid;

    private Integer eid;

    private Integer uid;

    private String reason;

    private Integer status;

    private Date reporttime;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getReporttime() {
        return reporttime;
    }

    public void setReporttime(Date reporttime) {
        this.reporttime = reporttime;
    }
}