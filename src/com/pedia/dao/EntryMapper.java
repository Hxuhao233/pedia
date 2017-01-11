package com.pedia.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pedia.model.Entry;

public interface EntryMapper {
    int deleteByPrimaryKey(Integer eid);

    int insert(Entry record);

    int insertSelective(Entry record);

    Entry selectByPrimaryKey(Integer eid);

    int updateByPrimaryKeySelective(Entry record);

    int updateByPrimaryKey(Entry record);

    int addOneByPrimaryKey(@Param("eid")Integer eid,@Param("field")String field);



	Entry selectByAllEntryName(String info);

	List<Entry> selectByInfo(String info);

	List<Entry> selectByStatus(Integer status);
}