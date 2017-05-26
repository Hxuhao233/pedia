package com.pedia.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pedia.tool.EntryInfo;
import com.pedia.utils.JsonUtils;

import redis.clients.jedis.Jedis;

@Component
@Aspect
public class RedisAspect {
	/*
	private RedisConnection redisCache = RedisManager.getInstance().getConnection();
		
	 @Pointcut("execution(* com.pedia.service.IEntryService.enterEntry(java.lang.Integer))")    //进入词条
	 public void enterEntry(){
	        
	 }
	 
		
	 @Pointcut("execution(* com.pedia.service.IEntryService.enterEntry(java.lang.String))")    //进入词条
	 public void enterEntryDirectly(){
	        
	 }
	 
	 @Pointcut("execution(* com.pedia.service.IManagerService.checkModifiedEntry(..))")    //管理员审核词条
	 public void modifyEntry(){
	        
	 }
	 
	 @Around("enterEntryDirectly()")
	 public EntryInfo aroundEnterEntryDirectly(ProceedingJoinPoint joinPoint){
		 	System.out.println("before enter Entry ");
			
			
			// 获取参数
			String info = null;
			Object[] args = joinPoint.getArgs();
			if(args !=null && args.length>0){
				info = (String) args[0];
			}
			String jsonEntryInfo = (String) redisCache.hget(info,"content");
			EntryInfo entryInfo = null;
			if(jsonEntryInfo!=null){
				entryInfo = JsonUtils.decode(jsonEntryInfo,new TypeReference<EntryInfo>() {});
				System.out.println("get from the redis cache");
				return entryInfo;
			}
			
			// 从数据库中查询
			try {
				entryInfo = (EntryInfo)joinPoint.proceed();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// 更新缓存
			 System.out.println("after enter Entry directly");
			 redisCache.hset(info, "content",JsonUtils.encode(entryInfo));
			 return entryInfo;
		 
	 }
	 
	 
	 @Around("enterEntry()")
	 public EntryInfo around(ProceedingJoinPoint joinPoint){
		System.out.println("before enter Entry ");
		
		
		// 获取参数
		Integer info = null;
		Object[] args = joinPoint.getArgs();
		if(args !=null && args.length>0){
			info = (Integer) args[0];
		}
		String jsonEntryInfo = (String) redisCache.hget("entry","content");
		EntryInfo entryInfo = null;
		if(jsonEntryInfo!=null){
			entryInfo = JsonUtils.decode(jsonEntryInfo,new TypeReference<EntryInfo>() {});
			System.out.println("get from the redis cache");
			return entryInfo;
		}
		
		// 从数据库中查询
		try {
			entryInfo = (EntryInfo)joinPoint.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// 更新缓存
		 System.out.println("after enter Entry");
		 redisCache.hset("Entry:" + info, "content",JsonUtils.encode(entryInfo));
		 return entryInfo;
	 
	 }
	 
	 @Around("modifyEntry()")
	 public int modify(ProceedingJoinPoint joinPoint){
		 	// 获取参数
			Integer eid = null;
			Boolean allow = null;
			Object[] args = joinPoint.getArgs();
			if(args !=null && args.length>0){
				eid = (Integer) args[0];
				allow = (Boolean) args[2];
			}

			int ret = 0;
			try {
				ret = (int) joinPoint.proceed();
				if( ret > 0 && allow){
					System.out.println("delete key" + eid);
					redisCache.del("Entry:" + eid);
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;
			
		
			
	 }
	 */
}
