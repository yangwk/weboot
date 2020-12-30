package com.github.yangwk.weboot.test.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.yangwk.weboot.common.lock.DistributedLock;

@Service
public class DistributedLockServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(DistributedLockServiceTest.class);

	@Autowired
	private OtherDistributedLockServiceTest otherDistributedLockServiceTest;
	
	@DistributedLock(lockName = "action")
	public Object action() {
		LOG.info("action before");
		try {
			Thread.sleep(20*1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		LOG.info("action after");
		action2();
		return Collections.singletonMap("name", "boy");
	}
	
	@DistributedLock(lockName = "action")
	public void action2() {
		LOG.info("action2 begin");
		try {
			Thread.sleep(1*1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		LOG.info("action2 end");
	}
	
	
	@DistributedLock(lockName = "asyncAction")
	public void asyncAction() {
		LOG.info("asyncAction begin");
		try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		LOG.info("asyncAction end");
	}
	
	
	@Async
	public void asyncLock() {
		LOG.info("asyncLock");
		actionInLock();
	}
	
	public void doTest() {
		LOG.debug("doTest");
		actionInLock();
	}
	
	@DistributedLock(lockName = "actionInLock")
	public Object actionInLock() {
		LOG.info("actionInLock");
		return otherDistributedLockServiceTest.actionInLock();
	}
	
	@DistributedLock(lockName = "actionInLockOther")
	public Object actionInLockOther() {
		LOG.info("actionInLockOther");
		return otherDistributedLockServiceTest.actionInLock();
	}
	
	
}
