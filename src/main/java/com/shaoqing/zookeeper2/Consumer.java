package com.shaoqing.zookeeper2;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {
    private final static Logger logger = LoggerFactory.getLogger(Consumer.class); 
	public static void main(String[] args) {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
	                "classpath*:consumer.xml");
	        context.start();
	        DemoService demoService = (DemoService)context.getBean("demoService"); // 获取远程服务
	        String hello = demoService.sayHello("world"); // 执行远程方法
	        System.out.println( hello ); // 显示调用结果
	}
}
