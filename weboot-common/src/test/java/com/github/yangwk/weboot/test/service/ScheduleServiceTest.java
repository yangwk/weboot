package com.github.yangwk.weboot.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.yangwk.weboot.common.lock.DistributedLock;

@Service
public class ScheduleServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleServiceTest.class);

	@Autowired
	private AsyncServiceTest asyncServiceTest;
	@Autowired
	private OtherDistributedLockServiceTest otherDistributedLockServiceTest;

	@Scheduled(cron = "0 44 19 * * ?")
	public void query() {
		asyncServiceTest.async1();
	}

	@DistributedLock(lockName = "queryTwo")
	@Scheduled(cron = "0 01 19 * * ?")
	public void queryTwo() {
		LOG.info("queryTwo");
		try {
			Thread.sleep(60*1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		asyncServiceTest.doQueryName();
	}
	
	@DistributedLock(lockName = "queryTwo")
	@Scheduled(cron = "0 01 19 * * ?")
	public void queryTwo2() {
		LOG.info("queryTwo2");
		try {
			Thread.sleep(60*1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		asyncServiceTest.doQueryName();
	}
	
	@Scheduled(cron = "0 10 18 * * ?")
	@DistributedLock(lockName = "ScheduleddoLock")
	public void doLock() {
		LOG.info("doLock");
		otherDistributedLockServiceTest.actionInLock();
	}

}
