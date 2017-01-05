package com.pedia.test;

import org.junit.Ignore;
import org.junit.Test;
import org.mybatis.spring.submitted.xa.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.pedia.entity.Student;
import com.pedia.service.IUserService;

import junit.framework.Assert;

public class RedisTest {
	public static ApplicationContext ctx = new ClassPathXmlApplicationContext(
			new String[] { "spring-mybatis.xml", "spring-redis.xml" });

	public static JedisConnectionFactory jedisConnetionFactory = (JedisConnectionFactory) ctx
			.getBean("connectionFactory");

	public RedisTemplate<String, Student> redisTemplate = (RedisTemplate<String, Student>) ctx.getBean("redisTemplate");

	public RedisSerializer<String> serializer = (RedisSerializer<String>) ctx.getBean("stringSerializer");

	public Jackson2JsonRedisSerializer<Student> JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Student.class);

	public IUserService userService = (IUserService) ctx.getBean("PediaUserService");

	@SuppressWarnings("deprecation")
	// @Test
	public void testCacheList() {
		String key = "spring";
		ValueOperations<String, Student> lov = redisTemplate.opsForValue();

		// redisTemplate.setKeySerializer(serializer);
		// redisTemplate.setValueSerializer(JsonRedisSerializer);
		// rt.setDefaultSerializer(serializer);

		Student s1 = new Student();
		s1.setAccount("13710685836");
		s1.setName("何徐昊");
		s1.setPassword("2333");
		lov.set(s1.getAccount(), s1);
		long size = lov.size(key); // rt.boundListOps(key).size();
		// Assert.assertEquals(1, size);
	}

	// 测试便捷对象StringRedisTemplate

	@SuppressWarnings("deprecation")
	// @Test
	public void test5() {
		ValueOperations<String, Student> vop = redisTemplate.opsForValue();
		String key = "13710685836";

		Student value = vop.get(key);
		System.out.println(value);
	}

	/*
	@Test
	public void test6() {
		System.out.println(userService.getClass());
		Student s = userService.queryUser("13710685839");

		Student s2 = userService.queryUser("13710685839");
		if (s != null) {
			System.out.println(s.toString());
		}
		if (s2 != null) {
			System.out.println(s2.toString());
		}
	}
	*/
}
