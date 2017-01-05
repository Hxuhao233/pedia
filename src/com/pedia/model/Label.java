package com.pedia.model;

public class Label {
    private Integer lid;

    private Integer eid;

    private String labelcontent;

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getLabelcontent() {
        return labelcontent;
    }

    public void setLabelcontent(String labelcontent) {
        this.labelcontent = labelcontent == null ? null : labelcontent.trim();
    }
}