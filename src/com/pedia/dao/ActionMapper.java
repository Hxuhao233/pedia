package com.pedia.dao;

import java.util.List;

import com.pedia.model.Action;

public interface ActionMapper {
    int deleteByPrimaryKey(Integer aid);

    int insert(Action record);

    int insertSelective(Action record);

    Action selectByPrimaryKey(Integer aid);
    
    List<Action> selectByEid(Integer eid);
    
    Action selectByKey(Integer neweid);
    
    List<Action> selectByStatus(Integer status);

    int updateByPrimaryKeySelective(Action record);

    int updateByPrimaryKey(Action record);
}