package com.pedia.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.pedia.redis.RedisConnection;
import com.pedia.redis.RedisManager;



public class RedisHttpSessionProxy implements InvocationHandler{

	
	private Object target;
	private RedisHttpSessionRepository repository = RedisHttpSessionRepository.getInstance();
	public Object bind(Object target){
		this.target = target;
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		RedisHttpSession redisHttpSession = (RedisHttpSession) target;
		RedisConnection connection = redisHttpSession.getRedisConnection();
		if(!connection.isConnected()){
			connection.close();
			redisHttpSession.setRedisConnection(repository.getRedisConnection());
		}
		////System.out.println("inner invoke " + redisHttpSession.getKey());
		if(redisHttpSession.isInvalidated()){
			
			throw new IllegalStateException("this session has been invalidated!");
	
		}else{
			System.out.println(method.getName());
			if(args!=null){
				for(int i=0;i<args.length;i++)
					System.out.println(args[i].toString());
			}
			Object result = method.invoke(target, args);
			////System.out.println(method.getName() + " fin result : " + result.toString());
			if(!method.equals("invalidate")){
				redisHttpSession.setLastAccessTime(System.currentTimeMillis());
				redisHttpSession.refresh();
			}
			return result;
		}

		
	}

}
