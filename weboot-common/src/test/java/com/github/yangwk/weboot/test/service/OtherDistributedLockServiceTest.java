package com.github.yangwk.weboot.test.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.yangwk.weboot.common.lock.DistributedLock;

@Service
public class OtherDistributedLockServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(OtherDistributedLockServiceTest.class);

	@DistributedLock(lockName = "actionInLock", waitTime=5000, leaseTime=30000)
	public Object actionInLock() {
		LOG.info("actionInLock");
		return Collections.singletonMap("pretty", "world");
	}

}
