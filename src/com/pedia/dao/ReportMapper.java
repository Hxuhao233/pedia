package com.pedia.dao;

import java.util.List;

import com.pedia.model.Report;

public interface ReportMapper {
    int deleteByPrimaryKey(Integer rid);

    int insert(Report record);

    int insertSelective(Report record);

    Report selectByPrimaryKey(Integer rid);
    
    List<Report> selectByStatus(Integer Status);
    
    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKey(Report record);
}