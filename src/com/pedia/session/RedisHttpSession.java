package com.pedia.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.pedia.redis.RedisConnection;


public class RedisHttpSession implements HttpSession{

    public static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 1800;

    public static final String SESSION_PREFIX = "session:";
    private static final String SESSION_ATTR = "sessionAttr:";
    private static final String CREATION_TIME = "creationTime";
    private static final String LAST_ACCESSED_TIME = "lastAccessedTime";
    private static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";
	
	private String id;
	private String key;
	private long createTime;
	private long lastAccessTime;
	private int maxInactiveInterval;
	
	
	private ServletContext ServletContext;
	
	private RedisConnection redisConnection;
	
	private RedisHttpSession() {

	}
	
	private RedisHttpSession(ServletContext servletContext, RedisConnection redisConnection){
		this.ServletContext = servletContext;
		this.redisConnection = redisConnection;
		id = UUID.randomUUID().toString();
		key = SESSION_PREFIX + id;
		createTime = System.currentTimeMillis();
		lastAccessTime = createTime;
		maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;
		saveSession();
	}
	
	public static RedisHttpSession createNew(ServletContext servletContext, RedisConnection redisConnection) {
		return new RedisHttpSession(servletContext, redisConnection);
	}
	
	// save to redis
	private void saveSession(){
		//System.out.println(key + " save to redis");
		redisConnection.hset(key, LAST_ACCESSED_TIME, lastAccessTime);
		redisConnection.hset(key, CREATION_TIME, createTime);
		redisConnection.hset(key, MAX_INACTIVE_INTERVAL, maxInactiveInterval);
		refresh();
	}

	public void refresh(){
		System.out.println(key + " refresh");
		redisConnection.expire(key, getMaxInactiveInterval());
	}
	
	public static RedisHttpSession createWithExistSession(String token,ServletContext servletContext,RedisConnection redisConnection){
		RedisHttpSession redisHttpSession = new RedisHttpSession();
		redisHttpSession.setId(token);
		redisHttpSession.setKey(SESSION_PREFIX + token);
		redisHttpSession.setServletContext(servletContext);
		redisHttpSession.setRedisConnection(redisConnection);
		
		return redisHttpSession;
	}
	
	public void setServletContext(ServletContext servletContext) {
		ServletContext = servletContext;
	}

	public void setRedisConnection(RedisConnection redisConnection) {
		this.redisConnection = redisConnection;
	}
	
	public RedisConnection getRedisConnection(){
		return redisConnection;
	}

	// put the info in the session
	@Override
	public void setAttribute(String name, Object value) {
		System.out.println("set " + key + " field :" + name + " value : " + value );
		redisConnection.hset(key, SESSION_ATTR + name, (Serializable)value);		
	}
	
	// get the info from the session
	@Override
	public Object getAttribute(String name) {
		System.out.println("get " + key + " field : " + name);
		return redisConnection.hget(key, SESSION_ATTR + name);
	}

	@Override
	public void removeAttribute(String name) {
		System.out.println("remove " + key + " field : " + name);
		redisConnection.hdel(key,SESSION_ATTR + name);
	}
	
	@Override
	public String[] getValueNames() {
		return getAttributeKeys().toArray(new String[0]);
	}
	
	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}
	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}
	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}
	
	@Override
	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(getAttributeKeys());
	}
	
	private Set<String> getAttributeKeys(){
		Set<String> keys = redisConnection.hkeys(key);
		Set<String> attrNames =  new HashSet<>();
		for(String key : keys){
			if(key.startsWith(SESSION_ATTR)){
				attrNames.add(key.substring(SESSION_ATTR.length()));
			}
		}
		
		return attrNames;
	}

	@Override
	public long getCreationTime() {
		return (long) redisConnection.hget(key, CREATION_TIME);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getLastAccessTime() {
		return (long) redisConnection.hget(key, LAST_ACCESSED_TIME);
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		return (long) redisConnection.hget(key, LAST_ACCESSED_TIME);
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		redisConnection.hset(key, MAX_INACTIVE_INTERVAL, DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS);
	}
	
	@Override
	public int getMaxInactiveInterval() {
		return (int) redisConnection.hget(key, MAX_INACTIVE_INTERVAL);
	}

	@Override
	public ServletContext getServletContext() {
		return ServletContext;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		redisConnection.del(key);
	}
	
	public boolean isValid(){
		//System.out.println(id + " : " + key );
		return redisConnection.exists(key);
	}

	@Override
	public boolean isNew() {
		return false;
	}








}
