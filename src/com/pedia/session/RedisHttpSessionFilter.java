package com.pedia.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedia.exception.SessionInvalidatedException;
import com.pedia.tool.ResponseData;

/**
 * Servlet Filter implementation class RedisHttpSessionFilter
 */
@WebFilter("/RedisHttpSessionFilter")
public class RedisHttpSessionFilter implements Filter {

	
	//private static final String TOKEN_HEADER_NAME = "x-auth-token";
	
	private static final String COOKIES_NAME = "JSESSIONID";

    private RedisHttpSessionRepository repository;

    public RedisHttpSessionFilter(){
        //repository = RedisHttpSessionRepository.getInstance();
    }


	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		/*
		try{
			RedisSessionRequestWrapper  requestWrapper = new RedisSessionRequestWrapper((HttpServletRequest)request);
			//System.out.println("RedisSessionRequestWrapper fin");
			RedisSessionResponseWrapper responseWrapper = new RedisSessionResponseWrapper((HttpServletResponse)response, requestWrapper);
			// pass the request along the filter chain
			chain.doFilter(requestWrapper, responseWrapper);
		} catch (SessionInvalidatedException e) {
			
			
			HttpServletRequest r1 = (HttpServletRequest)request;
			HttpServletResponse r = (HttpServletResponse) response;
			Cookie cookie = new Cookie(COOKIES_NAME, null);
			cookie.setMaxAge(0);
			cookie.setPath(r1.getContextPath());
			System.out.println("clear cookies");
			r.addCookie(cookie);
			RedisSessionRequestWrapper  requestWrapper = new RedisSessionRequestWrapper(r1);
			//System.out.println("RedisSessionRequestWrapper fin");
			RedisSessionResponseWrapper responseWrapper = new RedisSessionResponseWrapper(r, requestWrapper);
			/*
			System.out.println(e.getErrMsg());
			
			HttpServletRequest r1 = (HttpServletRequest)request;
			HttpServletResponse r = (HttpServletResponse) response;
			
			Cookie cookie = new Cookie(COOKIES_NAME, null);
			cookie.setMaxAge(0);
			cookie.setPath(r1.getContextPath());
			System.out.println("clear cookies");
			r.addCookie(cookie);
			ResponseData exceptionData = new ResponseData();
			exceptionData.setCode(1000);
			Map<String,Object> data = new HashMap<>();
			String info = "由于长时间未操作,您已离线-0-,请重新登录";
			data.put("info", info);
			exceptionData.setData(data);
			System.out.println("encode :" + new ObjectMapper().writeValueAsString(exceptionData));
			
			response.setContentType("application/json;charset=UTF-8");
			//response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			pw.write(new ObjectMapper().writeValueAsString(exceptionData));
			pw.flush();
			pw.close();
			*/
		/*}*/
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	
	private final class RedisSessionRequestWrapper extends HttpServletRequestWrapper{

        private HttpServletRequest request;
        //private String token;
        private String sessionId;
        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request
         * @throws IllegalArgumentException if the request is null
         */
        public RedisSessionRequestWrapper(HttpServletRequest request) {
            super(request);
            this.request = request;
            //this.token = request.getHeader(TOKEN_HEADER_NAME);
            //System.out.println("request get header " + this.token);
            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
	            for(int i=0;i<cookies.length;i++){
	            	if(cookies[i].getName().equals(COOKIES_NAME)){
	            		this.sessionId = cookies[i].getValue();
	            		break;
	            	}
	            }
	           }
            
        }

        @Override
        public HttpSession getSession(boolean create) {
        	System.out.println("getSession : " + sessionId);
            if (/*token != null*/ sessionId !=null) {
                return repository.getSession(sessionId, request.getServletContext());
            } else if (create){
            	System.out.println("create new session");
                HttpSession session = repository.newSession(request.getServletContext());
                sessionId = session.getId();
                
                //System.out.println("request getSession token2 : " + token);
                return session;
            } else {
                return null;
            }
			
			
        }

        @Override
        public HttpSession getSession() {
        	//System.out.println("RedisSessionRequestWrapper getSession" );
            return getSession(true);
        }

        @Override
        public String getRequestedSessionId() {
            return sessionId;
        }
    }

    private final class RedisSessionResponseWrapper extends HttpServletResponseWrapper {
        
    	private HttpServletResponse response;
    	/**
         * Constructs a response adaptor wrapping the given response.
         *
         * @param response
         * @throws IllegalArgumentException if the response is null
         */
        public RedisSessionResponseWrapper(HttpServletResponse response, RedisSessionRequestWrapper request) {
            super(response);
            //if session associate with token is not existed, create one for the response
            //System.out.println("response set header start ");
            this.response = response;
            //response.setHeader(TOKEN_HEADER_NAME, request.getSession(true).getId());
            //System.out.println("response set header fin ");
            //if(request.getRequestedSessionId() == null){
            System.out.println("add cookies");
        	Cookie cookie = new Cookie(COOKIES_NAME,request.getSession(true).getId());
        	cookie.setPath(request.getContextPath());
        	System.out.println(request.getContextPath());
        	this.response.addCookie(cookie);
        	System.out.println("=====================================");
            //}
        }
    }

}
