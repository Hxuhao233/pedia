package com.pedia.dao;

import java.util.List;

import com.pedia.model.Label;

public interface LabelMapper {
    int deleteByPrimaryKey(Integer lid);

    int insert(Label record);

    int insertSelective(Label record);

    Label selectByPrimaryKey(Integer lid);
    
    List<Label> selectByEid(Integer eid);

    int updateByPrimaryKeySelective(Label record);

    int updateByPrimaryKey(Label record);
}