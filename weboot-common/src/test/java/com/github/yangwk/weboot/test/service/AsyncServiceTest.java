package com.github.yangwk.weboot.test.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.yangwk.weboot.test.bean.TestBean;

@Service
public class AsyncServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(AsyncServiceTest.class);
	
	@Autowired
	private OtherAsyncServiceTest otherAsyncServiceTest;
	
	public TestBean queryBean() {
		LOG.info("queryBean");
		TestBean bean = new TestBean();
		bean.setId(16354654534L);
		bean.setAmount(new BigDecimal("4684646.12"));
		bean.setCount(89);
		bean.setName("这是一个测试的bean");
		return bean;
	}
	
	public String doQueryName() {
		LOG.info("doQueryName");
		return "my name is Hello";
	}
	
	@Async
	public String async1() {
		LOG.info("async1");
		return doQueryName();
	}
	
	@Async
	public String async2() {
		LOG.info("async2");
		return async2Bridge();
	}
	
	public String async2Bridge() {
		LOG.info("async2Bridge");
		return async2Again();
	}
	
	@Async
	public String async2Again() {
		LOG.info("asyncAgain");
		return "my name is Love";
	}
	
	@Async
	public void crossServiceAsync() {
		LOG.info("crossServiceAsync");
		otherAsyncServiceTest.async1();
	}
	
}
