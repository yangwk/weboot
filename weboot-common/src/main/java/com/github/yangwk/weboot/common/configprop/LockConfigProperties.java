package com.github.yangwk.weboot.common.configprop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weboot.lock")
public class LockConfigProperties {
	private String lockNamePrefix;
	private long defaultWaitTime;
	private long defaultLeaseTime;

	public String getLockNamePrefix() {
		return lockNamePrefix;
	}

	public void setLockNamePrefix(String lockNamePrefix) {
		this.lockNamePrefix = lockNamePrefix;
	}

	public long getDefaultWaitTime() {
		return defaultWaitTime;
	}

	public void setDefaultWaitTime(long defaultWaitTime) {
		this.defaultWaitTime = defaultWaitTime;
	}

	public long getDefaultLeaseTime() {
		return defaultLeaseTime;
	}

	public void setDefaultLeaseTime(long defaultLeaseTime) {
		this.defaultLeaseTime = defaultLeaseTime;
	}


}
