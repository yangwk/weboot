package com.github.yangwk.weboot.common.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReentrantDistributedLockActionImpl implements DistributedLockAction {
	private static final Logger LOG = LoggerFactory.getLogger(ReentrantDistributedLockActionImpl.class);

	private RedissonClient redissonClient;
	private long defaultWaitTime;
	private long defaultLeaseTime;

	public ReentrantDistributedLockActionImpl(RedissonClient redissonClient, long defaultWaitTime,
			long defaultLeaseTime) {
		this.redissonClient = redissonClient;
		this.defaultWaitTime = defaultWaitTime;
		this.defaultLeaseTime = defaultLeaseTime;
	}

	@Override
	public <T, R> R doInLock(String lockName, T t, Function<T, R> function, long waitTime, long leaseTime) {
		RLock lock = redissonClient.getLock(lockName);

		if (lock.isHeldByCurrentThread()) {
			LOG.debug("lockName {} is held by current thread", lockName);
			return function.apply(t);
		}

		try {
			if (waitTime < 0) {
				waitTime = this.defaultWaitTime;
			}
			if (leaseTime < 0) {
				leaseTime = this.defaultLeaseTime;
			}
			LOG.debug("acquire lock {} waitTime {} leaseTime {}", lockName, waitTime, leaseTime);

			if (lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS)) {
				try {
					return function.apply(t);
				} finally {
					if (lock.isHeldByCurrentThread()) {
						try {
							lock.unlock();
						} catch (Exception e) {
							LOG.warn("unlock {} unusually", lockName, e);
						}
					}
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		LOG.warn("can not acquire lock {}", lockName);
		return null;
	}

}
