package com.pedia.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.pedia.service.IEntryService;
import com.pedia.service.IUserService;

public class ServiceTest {
	public static void main(String[] args){
		ApplicationContext ac1 = new ClassPathXmlApplicationContext(new String[] { "spring-mybatis.xml", "spring-redis.xml" });
		IEntryService entryService = (IEntryService) ac1.getBean("entryService");
		entryService.enterEntry("c语言");
	}
}
