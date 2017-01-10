package com.pedia.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pedia.dao.UserMapper;

import com.pedia.model.Entry;
import com.pedia.model.User;
import com.pedia.service.IEntryService;
import com.pedia.service.IUserService;
import com.pedia.tool.BaseEntryDataList;
import com.pedia.tool.DetailedUserData;
import com.pedia.tool.RequestData;
import com.pedia.tool.ResponseData;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private IUserService UserService;
	@Autowired
	private IEntryService EntryService;
	
	@Autowired
	private UserMapper userDao;
	
	// 获取个人主页
	@ResponseBody
	@RequestMapping(value = "/getPersonalHomePage", method = RequestMethod.GET)
	public ResponseData getPersonalHomePage(HttpSession session,@RequestParam(value="uid",required=false)Integer uid) {
		User user=(User)session.getAttribute("user");

		ResponseData ret=new ResponseData();
		if (user!=null|| (user==null&&uid!=null))
		{

			Map<String,Object> data = new HashMap<String,Object>();
			DetailedUserData detailedUserData;
			if(uid!=null){
				User u = userDao.selectByPrimaryKey(uid);
				data.put("level", u.getLevel());//等级
				data.put("exp", u.getExp());//经验
				detailedUserData=UserService.enterPersonalHomePage(uid);
			}else{
				data.put("level", user.getLevel());//等级
				data.put("exp", user.getExp());//经验
				detailedUserData=UserService.enterPersonalHomePage(user.getUid());
			}


			List<Entry> entriesList=detailedUserData.getEntries();
			data.put("entriesNum",entriesList.size());//提交版本数
			List<Map<String, Object>> hasPassList=new ArrayList<>();
			List<Map<String, Object>> toPassList=new ArrayList<>();
			List<Map<String, Object>> hasNotPassList=new ArrayList<>();
			for (int i=0;i<entriesList.size();i++)
			{
				Entry entry=entriesList.get(i);
				switch (entry.getStatus())
				{
					case 2:
						//已通过
						Map<String, Object> hasPassMap=new HashMap<String, Object>();
						hasPassMap.put("eid", entry.getEid());//词条id
						hasPassMap.put("entryName", entry.getEntryname());//词条名称
						hasPassMap.put("createDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entry.getPublishtime()));//创建时间
						hasPassMap.put("passDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entry.getPublishtime()));//通过时间 //TODO 目前用创建时间，需要改成通过时间
						hasPassMap.put("modifyTimes", 0);//被他人修改次数 //TODO 目前用0替代，需要改成被修改次数
						hasPassList.add(hasPassMap);
						break;
						
					case 1:case 6:
						//待通过
						Map<String, Object> toPassMap=new HashMap<String, Object>();
						toPassMap.put("eid", entry.getEid());//词条id
						toPassMap.put("entryName", entry.getEntryname());//词条名称
						toPassMap.put("createDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entry.getPublishtime()));//创建时间
						toPassList.add(toPassMap);
						break;
				
					case 3:
						//未通过
						Map<String, Object> hasNotPassMap = new HashMap<String, Object>();
						hasNotPassMap.put("eid", entry.getEid());//词条id
						hasNotPassMap.put("entryName", entry.getEntryname());//词条名称
						hasNotPassMap.put("createDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entry.getPublishtime()));//创建时间
						hasNotPassMap.put("refuseReason", entry.getRefusereason());
						hasNotPassList.add(hasNotPassMap);
						break;
				}
			}
			data.put("hasPassNum", hasPassList.size());//已通过版本数
			data.put("toPassNum", toPassList.size());//已通过版本数
			data.put("hasNotPassNum", hasNotPassList.size());//未通过版本数
			data.put("hasPassList", hasPassList);//已通过版本列表
			data.put("toPassList", toPassList);//待通过版本列表
			data.put("hasNotPassList", hasNotPassList);//未通过版本列表
			data.put("passRate", hasPassList.size()/ ( (hasPassList.size()+hasNotPassList.size())==0 ? 1 :hasPassList.size()+hasNotPassList.size()  )        );                //通过率
			data.put("username", user.getUsername());
			ret.setData(data);
			System.out.println("获取个人主页成功！");
			ret.setCode(200);
		}
		else {

			System.out.println("获取个人主页失败！session为null");
			ret.setCode(500);
		}
		return ret;
	}
	
	//删除词条
	@RequestMapping(value = "/deleteEntry/{eid}", method = RequestMethod.GET)
	public @ResponseBody ResponseData deleteEntry(@PathVariable("eid") int eid, HttpSession session) {
		User user=(User)session.getAttribute("user");
		ResponseData ret=new ResponseData();
		if (user!=null){
			//检测该用户是否拥有该词条
			List<Entry> entriesList=UserService.getUserEntries(user.getUid());
			ret.setCode(500);
			for (int i=0;i<entriesList.size();i++)
			{
				if (entriesList.get(i).getEid()==eid)//证实了该用户确实拥有该词条
				{
					EntryService.deleteEntry(eid);
					ret.setCode(200);
					break;
				}
			}
			if (ret.getCode()==200){
				System.out.println("删除词条成功！");
			}
			else {
				System.out.println("删除词条失败！该用户没有该词条");
			}
		}
		else {
			System.out.println("删除词条失败！session为null");
			ret.setCode(500);
		}
		return ret;
	}
	
	// 用户注册
		@RequestMapping(value = "/signup", method = RequestMethod.POST)
		public @ResponseBody ResponseData signUp(@RequestBody User user) {
			if(user.getUsername()!=null){
				System.out.println("名字:"  + user.getUsername());
			}
			int id = UserService.register(user);
			ResponseData ret=new ResponseData();
			Map<String, Object> data=new HashMap<>();
			if (id > 0) {
				ret.setCode(200);
				data.put("info", "注册成功");
				ret.setData(data);
			} else {
				ret.setCode(500);
				data.put("info", "注册失败,用户名已存在");
				ret.setData(data);
			}
			return ret;
		}
		
		//用户登录
		@RequestMapping(value = "/login", method = RequestMethod.POST)
		public @ResponseBody ResponseData login(@RequestBody RequestData userData, HttpSession session) {
			ResponseData ret=new ResponseData();
			Map<String, Object>data=new HashMap<>();
			User hasLoginUser=(User)session.getAttribute("user");
			String account = userData.getData().get("account");
			String password = userData.getData().get("password");
			if (hasLoginUser != null) {
				ret.setCode(500);
				data.put("info", "用户已登录");
			}else if(account != null && password != null && !"".equals(account) && !"".equals(password)){
				User toLoginUser=UserService.login(account, password);
				if (toLoginUser!=null){
					session.setAttribute("user", toLoginUser);
					ret.setCode(200);
					data.put("info", "登录成功");
					data.put("exp", toLoginUser.getExp());
					data.put("iconaddr", toLoginUser.getIconaddr());
					data.put("lastLoginTime", new SimpleDateFormat("yyyy-MM-dd").format(toLoginUser.getLastlogintime()));
					data.put("level", toLoginUser.getLevel());
					data.put("userName", toLoginUser.getUsername());
					data.put("role", toLoginUser.getRole());
				}else {
					ret.setCode(500);
					data.put("info", "用户名或密码错误");
				}	
			}else {
				ret.setCode(500);
				data.put("info", "未输入用户名或密码");
			}
			ret.setData(data);
			return ret;
		}
		
		//用户注销
		@ResponseBody
		@RequestMapping(value = "/logout", method = RequestMethod.GET)
		public ResponseData logout (HttpSession session){
			ResponseData ret=new ResponseData();
			Map<String, Object> data = new HashMap<>();
			User user=(User)session.getAttribute("user");
			if (user!=null){
				session.removeAttribute("user");
				ret.setCode(200);
				data.put("info", "注销成功");
			}else {
				ret.setCode(500);
				data.put("info", "注销失败，该用户没有登录");
			}
			ret.setData(data);
			return ret;
			
		}
	
		@ResponseBody
		@RequestMapping(value="uploadIcon")
		public ResponseData uploadIcon(@RequestParam(value="file" ,required=true) MultipartFile file,HttpSession session,HttpServletRequest request){
			ResponseData response = new ResponseData();
			
			User u = (User) session.getAttribute("user");
			int userId = u!=null ? u.getUid() : 2;
			
			String pathRoot = request.getSession().getServletContext().getRealPath("/");
			if(UserService.uploadIcon(userId, pathRoot, file)>0){
				response.setCode(200);
			}else{
				response.setCode(500);
			}
			
			return response;
		}
}