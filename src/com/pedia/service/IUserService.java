package com.pedia.service;


import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import com.pedia.model.Comment;
import com.pedia.model.Entry;
import com.pedia.model.Label;
import com.pedia.model.Report;
import com.pedia.model.User;
import com.pedia.tool.BaseEntryDataList;
import com.pedia.tool.DetailedEntryData;
import com.pedia.tool.DetailedUserData;

public interface IUserService {

/*	
	Student queryUser(String account);
	void flush();
	public Student updateUser(Student newRecord);
	*/
	
	User login(String account,String password);	// 登录
	int register(User u);													// 注册
	int logout();																// 注销
	int uploadIcon(int userId, String path, MultipartFile file);																		// 上传头像
	

	DetailedUserData enterPersonalHomePage(int uid); 				// 进入个人主页
	List<Entry> getUserEntries(int uid);  //获取用户词条


	
}
