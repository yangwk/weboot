package com.github.yangwk.weboot.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Async
@Service
public class OtherAsyncServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(OtherAsyncServiceTest.class);
	
	public void async1() {
		LOG.info("async1");
	}
}
