package com.pedia.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pedia.model.Action;

public interface ActionMapper {
    int deleteByPrimaryKey(Integer aid);

    int insert(Action record);

    int insertSelective(Action record);

    Action selectByPrimaryKey(Integer aid);
    
    List<Action> selectByEidAndStatus(@Param("eid")Integer eid,@Param("status")Integer status);

    List<Action> selectByEid(Integer eid);
    
	List<Action> selectByStatus(Integer status);

	List<Action> selectByStatusAndType(@Param("status")Integer status , @Param("type")Integer type);

	List<Action> selectByUid(int uid);
	
    int updateByPrimaryKeySelective(Action record);

    int updateByPrimaryKey(Action record);
}