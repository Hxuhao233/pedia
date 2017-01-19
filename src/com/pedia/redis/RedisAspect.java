package com.pedia.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.pedia.tool.EntryInfo;

@Component
@Aspect
public class RedisAspect {
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, EntryInfo> redisCache;
	
	 @Pointcut("execution(* com.pedia.service.IEntryService.enterEntry(java.lang.String))")    //直接进入词条
	 public void myPointCut(){
	        
	 }
	 
	 @Around("myPointCut()")
	 public EntryInfo around(ProceedingJoinPoint joinPoint){
		System.out.println("before enter Entry ");
		 
		
		// 获取参数
		String info = null;
		Object[] args = joinPoint.getArgs();
		if(args !=null && args.length>0){
			info = (String) args[0];
		}
		EntryInfo entryInfo = redisCache.opsForValue().get("Entry:" + info);
		if(entryInfo!=null){
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
		 redisCache.opsForValue().set("Entry:" + info, entryInfo);
		 return entryInfo;
	 
	 }
}
