package com.pedia.dao;

import java.util.List;

import com.pedia.model.Comment;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer cid);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer cid);
    
    List<Comment> selectByEid(Integer eid);
    
    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
}