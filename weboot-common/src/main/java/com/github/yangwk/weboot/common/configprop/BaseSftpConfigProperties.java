package com.github.yangwk.weboot.common.configprop;

public abstract class BaseSftpConfigProperties {
	private ConnectConfig connectConfig;
	private PoolConfig poolConfig;

	public ConnectConfig getConnectConfig() {
		return connectConfig;
	}

	public void setConnectConfig(ConnectConfig connectConfig) {
		this.connectConfig = connectConfig;
	}

	public PoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(PoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public static class ConnectConfig {
		private String username;
		private String host;
		private int port;
		private String password;
		private int sessionConnectTimeout;
		private int sessionReadTimeout;
		private int sftpConnectTimeout;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public int getSessionConnectTimeout() {
			return sessionConnectTimeout;
		}

		public void setSessionConnectTimeout(int sessionConnectTimeout) {
			this.sessionConnectTimeout = sessionConnectTimeout;
		}

		public int getSessionReadTimeout() {
			return sessionReadTimeout;
		}

		public void setSessionReadTimeout(int sessionReadTimeout) {
			this.sessionReadTimeout = sessionReadTimeout;
		}

		public int getSftpConnectTimeout() {
			return sftpConnectTimeout;
		}

		public void setSftpConnectTimeout(int sftpConnectTimeout) {
			this.sftpConnectTimeout = sftpConnectTimeout;
		}

	}

	public static class PoolConfig {
		private boolean blockWhenExhausted;
		private long evictorShutdownTimeoutMillis;
		private long maxWaitMillis;
		private long minEvictableIdleTimeMillis;
		private long timeBetweenEvictionRunsMillis;
		private int maxIdle;
		private int maxTotal;
		private boolean jmxEnabled;
		private String jmxNamePrefix;
		private boolean testWhileIdle;

		public boolean isTestWhileIdle() {
			return testWhileIdle;
		}

		public void setTestWhileIdle(boolean testWhileIdle) {
			this.testWhileIdle = testWhileIdle;
		}

		public boolean isJmxEnabled() {
			return jmxEnabled;
		}

		public void setJmxEnabled(boolean jmxEnabled) {
			this.jmxEnabled = jmxEnabled;
		}

		public String getJmxNamePrefix() {
			return jmxNamePrefix;
		}

		public void setJmxNamePrefix(String jmxNamePrefix) {
			this.jmxNamePrefix = jmxNamePrefix;
		}

		public boolean isBlockWhenExhausted() {
			return blockWhenExhausted;
		}

		public void setBlockWhenExhausted(boolean blockWhenExhausted) {
			this.blockWhenExhausted = blockWhenExhausted;
		}

		public long getEvictorShutdownTimeoutMillis() {
			return evictorShutdownTimeoutMillis;
		}

		public void setEvictorShutdownTimeoutMillis(long evictorShutdownTimeoutMillis) {
			this.evictorShutdownTimeoutMillis = evictorShutdownTimeoutMillis;
		}

		public long getMaxWaitMillis() {
			return maxWaitMillis;
		}

		public void setMaxWaitMillis(long maxWaitMillis) {
			this.maxWaitMillis = maxWaitMillis;
		}

		public long getMinEvictableIdleTimeMillis() {
			return minEvictableIdleTimeMillis;
		}

		public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
			this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		}

		public long getTimeBetweenEvictionRunsMillis() {
			return timeBetweenEvictionRunsMillis;
		}

		public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
			this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
		}

		public int getMaxIdle() {
			return maxIdle;
		}

		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}

		public int getMaxTotal() {
			return maxTotal;
		}

		public void setMaxTotal(int maxTotal) {
			this.maxTotal = maxTotal;
		}

	}

}
