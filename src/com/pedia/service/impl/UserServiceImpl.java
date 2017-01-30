package com.pedia.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pedia.dao.ActionMapper;
import com.pedia.dao.CommentMapper;
import com.pedia.dao.EntryMapper;
import com.pedia.dao.ReportMapper;
import com.pedia.dao.UserMapper;

import com.pedia.model.Action;
import com.pedia.model.Comment;
import com.pedia.model.Entry;
import com.pedia.model.Report;
import com.pedia.model.User;
import com.pedia.service.IUserService;
import com.pedia.tool.BaseEntryDataList;
import com.pedia.tool.DetailedEntryData;
import com.pedia.tool.DetailedUserData;

@Service("PediaUserService")
public class UserServiceImpl implements IUserService {
	

	@Autowired
	private UserMapper userDao;
	
	@Autowired
	private EntryMapper entryDao;
	
	@Autowired
	private ActionMapper actionDao;

	/*
	@Autowired
	public StudentMapper userDao;
	
	@Override
	@Cacheable(value = "user", key = "'account_'+#account") // 使用了一个缓存名叫
															// userCache
	public Student queryUser(String account) {
		// TODO Auto-generated method stub
		// 方法内部实现不考虑缓存逻辑，直接实现业务
		System.out.println("real query account." + account);
		// ListOperations<String, Student> lop = redisTemplate.opsForList();
		Student u = userDao.selectByAccount(account);
		// lop.leftPush(u.getAccount(), u);
		return u;
	}

	// 清空缓存
	@CacheEvict(value = "user", allEntries = true)
	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	@CachePut(value = "user", key = "'account_'+#newRecord.account")
	public Student updateUser(Student newRecord) {
		// TODO Auto-generated method stub
		userDao.updateByPrimaryKeySelective(newRecord);
		return newRecord;
	}
*/
	
	@SuppressWarnings("deprecation")
	private boolean shouldAddLoginExp(Date lastlogintime,Date now) {
		boolean ret=false;
		if (lastlogintime.getDay()<now.getDay()) ret=true;
		if (lastlogintime.getMonth()<now.getMonth()) ret=true;
		if (lastlogintime.getYear()<now.getYear()) ret=true;
		return ret;
	}
	
	@Override
	public User login(String account, String password) {   //登录 输入帐号密码，输出 成功：登录的User对象 失败：null
		Map<String, String> param = new HashMap<String, String>();
		param.put("account", account);
		param.put("password", password);
		User u=userDao.selectByAccountAndPassword(param);
		if (u != null){
			Date date=new Date();//获取系统当前日期
			if (shouldAddLoginExp(u.getLastlogintime(), date)){
				u.AddExp(2);
				u.setLastlogintime(date);
				userDao.updateByPrimaryKeySelective(u);
				System.out.println("用户"+u.getAccount()+"获得每日登录经验2点");
			}
			else {
				u.setLastlogintime(date);
				userDao.updateByPrimaryKeySelective(u);
			}
			System.out.println("登录成功！用户account："+ account);
			return u;
		}
		else {
			System.out.println("登录失败！用户account："+ account);
			return null;
		}
		
	}

	@Override
	public int register(User u) {   	//注册 输入待注册User对象，输出 成功：注册用户的ID 失败：0
		int ret = 0;
		if (userDao.selectAccount(u.getAccount()) == 0)	{
			System.out.println(u.getAccount());
			userDao.insertSelective(u);
			ret=u.getUid();
			System.out.println("注册成功！用户account："+u.getAccount());
		}
		else System.out.println("注册失败！用户account："+u.getAccount()+"已存在");
		return ret;
	}

	@Override
	public int logout() {
		// TODO Auto-generated method stub
		// 暂无其他需求
		
		return 0;
	}


	@Override
	public int uploadIcon(int userId, String pathRoot, MultipartFile file) {
		// TODO Auto-generated method stub
		//String filePath = new String();

		String path = "";
		if (!file.isEmpty()) {
			// 生成uuid作为文件名称
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 获得文件类型（可以判断如果不是图片，禁止上传）
			String contentType = file.getContentType();
			// 获得文件后缀名称
			String imageName = contentType.substring(contentType.indexOf("/") + 1);
			pathRoot += "../static/images/";
			path = uuid + "." + imageName.trim();

			try {
				file.transferTo(new File(pathRoot + path));

				System.out.println(pathRoot + path);

				// request.setAttribute("imagesPath", "../../static" + path);
				//filePath = "../../static/image/" + path;

				// 更新数据库
				User user = new User();
				user.setIconaddr(path);
				user.setUid(userId);
				userDao.updateByPrimaryKeySelective(user);
				
				return 1;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
		
		return 0;
	}

	

	
	@Override
	public DetailedUserData enterPersonalHomePage(int uid) { //获取个人主页 输入uid 输出主页对象
		DetailedUserData detailedUserData=new DetailedUserData();
		User user=userDao.selectByPrimaryKey(uid);
		if (user!=null){
			detailedUserData.setUser(user);
			
			List<Action> actionList = actionDao.selectByUid(uid);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			for(Action action : actionList){
				switch (action.getStatus()) {
				// 未审核
				case 1:
					Map<String,Object> entryInfo = new HashMap<String,Object>();
					Entry entry = entryDao.selectByPrimaryKey(action.getEid());
					entryInfo.put("eid", action.getAid());
					entryInfo.put("entryName", entry.getEntryname());
					entryInfo.put("createDate", simpleDateFormat.format(entry.getPublishtime()));
					detailedUserData.addUncheckedEntry(entryInfo);
					break;
				
				// 以通过	
				case 2:
					Map<String,Object> entryInfo2 = new HashMap<String,Object>();
					Entry entry2 = entryDao.selectByPrimaryKey(action.getEid());
					entryInfo2.put("eid", action.getAid());
					entryInfo2.put("entryName", entry2.getEntryname());
					entryInfo2.put("createDate", simpleDateFormat.format(entry2.getPublishtime()));
					entryInfo2.put("passDate", simpleDateFormat.format(action.getActiontime()));
					entryInfo2.put("modifiTimes", actionDao.selectByEidAndStatus(entry2.getEid(),2).size());
					detailedUserData.addPassEntry(entryInfo2);
					
					break;
				
				// 未通过
				case 3:
					Map<String,Object> entryInfo3 = new HashMap<String,Object>();
					Entry entry3 = entryDao.selectByPrimaryKey(action.getEid());
					entryInfo3.put("eid", action.getAid());
					entryInfo3.put("entryName", entry3.getEntryname());
					entryInfo3.put("createDate", simpleDateFormat.format(action.getActiontime()));
					entryInfo3.put("refuseReason", action.getRefusereason());
					detailedUserData.addModifiedEntry(entryInfo3);
					
					break;
					
				default:
					break;
				}
			}
			
		}
		else {
			System.out.println("获取主页失败！没有该用户");
		}
		
		
		
		return detailedUserData;
	}
	
	@Override
	public List<Action> getUserEntries(int uid) {
		List<Action> data=actionDao.selectByUid(uid);
		return data;
	}
}
