package com.github.yangwk.weboot.common.configuration;

import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.github.yangwk.weboot.common.aop.DistributedLockAop;
import com.github.yangwk.weboot.common.configprop.LockConfigProperties;
import com.github.yangwk.weboot.common.lock.DistributedLock;
import com.github.yangwk.weboot.common.lock.DistributedLockAction;
import com.github.yangwk.weboot.common.lock.ReentrantDistributedLockActionImpl;

@Configuration
@ConditionalOnClass(value = { RedissonClient.class })
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@EnableAspectJAutoProxy
@EnableConfigurationProperties(LockConfigProperties.class)
public class DistributedLockConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DistributedLockAction distributedLockAction(RedissonClient redissonClient, LockConfigProperties lockConfigProperties) {
		return new ReentrantDistributedLockActionImpl(redissonClient, lockConfigProperties.getDefaultWaitTime(),
				lockConfigProperties.getDefaultLeaseTime());
	}

	@Bean
	@ConditionalOnClass(value = { DistributedLock.class })
	@ConditionalOnMissingBean
	public DistributedLockAop distributedLockAop(DistributedLockAction distributedLockAction,
			LockConfigProperties lockConfigProperties) {
		return new DistributedLockAop(lockConfigProperties, distributedLockAction);
	}

}
