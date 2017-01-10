package com.pedia.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pedia.model.Entry;

public interface EntryMapper {
    int deleteByPrimaryKey(Integer eid);

    int insert(Entry record);

    int insertSelective(Entry record);

    Entry selectByPrimaryKey(@Param("eid") Integer eid);
    
    Entry selectByAllEntryName(@Param("entryName") String entryName,@Param("status") Integer status);
    
    List<Entry> selectByInfo(@Param("info")  String info,@Param("status") Integer status);

    int updateByPrimaryKeySelective(Entry record);

    int updateByPrimaryKey(Entry record);
    
    int addOne(@Param("eid") Integer eid, @Param("field") String field);

	int subtractOne(@Param("eid") Integer eid, @Param("field") String field);
	
	List<Entry> selectByUid(Integer uid);
	
	
	List<Entry> selectByEntryName(String entryName);
}