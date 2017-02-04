package com.pedia.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.pedia.exception.SessionInvalidatedException;
import com.pedia.redis.RedisConnection;
import com.pedia.redis.RedisManager;

public class RedisHttpSessionRepository {
 
	private RedisManager redisManager;

	private RedisConnection redisConnection;
	
	private final static RedisHttpSessionRepository instance = new RedisHttpSessionRepository();
	
	private RedisHttpSessionRepository(){
		this.redisManager = RedisManager.getInstance();
		this.redisConnection = redisManager.getConnection();
	}
	
	public static RedisHttpSessionRepository getInstance(){
		return instance;
	}
	
	public HttpSession newSession(ServletContext servletContext){
		checkConnection();
		RedisHttpSession redisHttpSession = RedisHttpSession.createNew(servletContext, redisConnection);
		//System.out.println("fin");
        return (HttpSession) new RedisHttpSessionProxy().bind(redisHttpSession);
	}
	
	/**
     * get session according to token
     *
     * @param token
     * @param servletContext
     * @return session associate to token or null if the token is invalid
     */
    public HttpSession getSession(String token, ServletContext servletContext) throws SessionInvalidatedException{
        checkConnection();
        if (redisConnection.exists(RedisHttpSession.SESSION_PREFIX + token)) {
            RedisHttpSession redisHttpSession = RedisHttpSession.createWithExistSession(token, servletContext, redisConnection);
            return (HttpSession) new RedisHttpSessionProxy().bind(redisHttpSession);
        } else {
            throw new SessionInvalidatedException("The HttpSession has already be invalidated.");
        }
    }

    public RedisConnection getRedisConnection() {
        checkConnection();
        return redisConnection;
    }

    // if connection is closed,get a new connection;
    private void checkConnection() {
        if (!redisConnection.isConnected()) {
            redisConnection.close();
            redisConnection = redisManager.getConnection();
        }
    }
 
}
