package com.github.yangwk.weboot.common.sftp;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.github.yangwk.weboot.common.configprop.BaseSftpConfigProperties;
import com.github.yangwk.weboot.common.configprop.BaseSftpConfigProperties.PoolConfig;
import com.jcraft.jsch.ChannelSftp;

public class SftpTemplateBuilder {

	public SftpTemplate build(BaseSftpConfigProperties baseSftpConfigProperties) {
		GenericObjectPoolConfig<ChannelSftp> channelPoolConfig = new GenericObjectPoolConfig<>();
		buildGenericObjectPoolConfig(channelPoolConfig, baseSftpConfigProperties.getPoolConfig());

		return new SftpTemplate(baseSftpConfigProperties.getConnectConfig(), channelPoolConfig);
	}

	private void buildGenericObjectPoolConfig(GenericObjectPoolConfig<?> config, PoolConfig pc) {
		config.setBlockWhenExhausted(pc.isBlockWhenExhausted());
		config.setEvictorShutdownTimeoutMillis(pc.getEvictorShutdownTimeoutMillis());
		config.setMaxWaitMillis(pc.getMaxWaitMillis());
		config.setMinEvictableIdleTimeMillis(pc.getMinEvictableIdleTimeMillis());
		config.setTimeBetweenEvictionRunsMillis(pc.getTimeBetweenEvictionRunsMillis());
		config.setMaxIdle(pc.getMaxIdle());
		config.setMaxTotal(pc.getMaxTotal());
		config.setJmxEnabled(pc.isJmxEnabled());
		config.setJmxNamePrefix(pc.getJmxNamePrefix());
		config.setTestWhileIdle(pc.isTestWhileIdle());
	}

}
