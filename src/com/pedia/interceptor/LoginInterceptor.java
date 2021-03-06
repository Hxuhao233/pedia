package com.pedia.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedia.model.User;
import com.pedia.tool.ResponseData;

/**
 * 登录拦截器
 * 
 * @author hxuhao
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	// 允许不登录就访问的内容
	private static final String[] IGNORE_URI = { "/index","/login","enterEntryDirectly","/signup","/readDetail","/readList.html","/readDetail.html", "/queryEntry","enterEntry", "/logout", "/images", "/register",
			"/scripts","/css","/seeEntry","/about" };
	
	// 允许管理员访问的内容
	private static final String[] MANAGER_URI = { "/manager"};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean flag = false;
		boolean flag2 = true;
		String url = request.getRequestURL().toString();
		//System.out.println(">>>: " + url);
		for (String s : IGNORE_URI) {
			if (url.contains(s)) {
				flag = true;
				//System.out.println("允许访问");
				return flag;
			}
		}
		if (!flag) {
			HttpSession s = request.getSession(false);
			if(s!=null){
				User user = (User) s.getAttribute("user");	
				if (user != null) {
					System.out.println(user.getRole());
					flag = true;
					//System.out.println("允许访问");
				}else{
					System.out.println("未登录");
				}
			}else{
				System.out.println("未知访问");
			}
		}

		
		for (String s : MANAGER_URI) {
			if (url.contains(s)) {
				User user = (User) request.getSession().getAttribute("user");
				if ( user==null || (user != null && user.getRole()<2) ) {
					
					flag2 = false;
					break;
				}else{
					flag2 = true;
					//System.out.println("允许访问");
				}
			}
		}

		// 不允许访问
		if (!flag||!flag2) {
			System.out.println(">>>: " + url + " no auth!");

			ObjectMapper mapper = new ObjectMapper();
			ResponseData responseData = new ResponseData();
			Map<String,Object> info = new HashMap<String,Object>();
			info.put("info", "无权访问,返回上一页");
			responseData.setCode(403);
			responseData.setData(info);

			//System.out.println(x);
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			pw.write(mapper.writeValueAsString(responseData));
			//pw.flush();
			
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}
