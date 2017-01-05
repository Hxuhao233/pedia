package com.pedia.dao;

import java.util.Map;

import com.pedia.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User selectByAccount(String account);
    
    int selectAccount(String account);
    
    User selectByAccountAndPassword(Map<String, String> accountAndPassword);
}