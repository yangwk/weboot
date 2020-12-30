package com.github.yangwk.weboot.common.aop;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncExecutionInterceptor;
import org.springframework.core.annotation.Order;

import com.github.yangwk.weboot.common.OrderedConstants;
import com.github.yangwk.weboot.common.configprop.LockConfigProperties;
import com.github.yangwk.weboot.common.lock.DistributedLock;
import com.github.yangwk.weboot.common.lock.DistributedLockAction;

/**
 * @see AsyncExecutionInterceptor#getOrder()
 *
 */
@Order(OrderedConstants.AopConstants.DISTRIBUTED_LOCK)
@Aspect
public class DistributedLockAop {
	private static final Logger LOG = LoggerFactory.getLogger(DistributedLockAop.class);

	private LockConfigProperties lockConfigProperties;
	private DistributedLockAction distributedLockAction;

	public DistributedLockAop(LockConfigProperties lockConfigProperties, DistributedLockAction distributedLockAction) {
		this.lockConfigProperties = lockConfigProperties;
		this.distributedLockAction = distributedLockAction;
	}

	@Around(value = "@annotation(distributedLock)")
	public Object execute(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("execute around annotation {} with lockName {} , waitTime {} , leaseTime {}", DistributedLock.class.getName(), distributedLock.lockName(), distributedLock.waitTime(), distributedLock.leaseTime());
		}

		String lockName = distributedLock.lockName();
		if (StringUtils.isBlank(lockName)) {
			throw new RuntimeException("lock name is empty");
		}
		lockName = lockConfigProperties.getLockNamePrefix() + lockName;

		Function<Void, Object> func = new Function<Void, Object>() {

			@Override
			public Object apply(Void t) {
				try {
					return joinPoint.proceed();
				} catch (Throwable e) {
					throw new RuntimeException("lock action error", e);
				}
			}
		};

		return distributedLockAction.doInLock(lockName, (Void) null, func, distributedLock.waitTime(), distributedLock.leaseTime());
	}

}
