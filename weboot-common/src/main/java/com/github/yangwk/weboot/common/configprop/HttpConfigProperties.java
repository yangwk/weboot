package com.github.yangwk.weboot.common.configprop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weboot.http")
public class HttpConfigProperties {
	private int maxConnPerRoute;
	private int maxConnTotal;
	private long connTimeToLive;
	private boolean evictExpiredAndIdleConnections;
	private long maxIdleTime;
	
	private HttpRequestConfig requestConfig;

	public boolean isEvictExpiredAndIdleConnections() {
		return evictExpiredAndIdleConnections;
	}

	public void setEvictExpiredAndIdleConnections(boolean evictExpiredAndIdleConnections) {
		this.evictExpiredAndIdleConnections = evictExpiredAndIdleConnections;
	}

	public int getMaxConnPerRoute() {
		return maxConnPerRoute;
	}

	public void setMaxConnPerRoute(int maxConnPerRoute) {
		this.maxConnPerRoute = maxConnPerRoute;
	}

	public int getMaxConnTotal() {
		return maxConnTotal;
	}

	public void setMaxConnTotal(int maxConnTotal) {
		this.maxConnTotal = maxConnTotal;
	}

	public long getConnTimeToLive() {
		return connTimeToLive;
	}

	public void setConnTimeToLive(long connTimeToLive) {
		this.connTimeToLive = connTimeToLive;
	}

	public long getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public HttpRequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(HttpRequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}
	
	public static class HttpRequestConfig {
        private boolean authenticationEnabled;
        private int connectionRequestTimeout;
        private int connectTimeout;
        private int readTimeout;
        private int retryCount;
        private long retryInterval;
        
		public long getRetryInterval() {
			return retryInterval;
		}
		public void setRetryInterval(long retryInterval) {
			this.retryInterval = retryInterval;
		}
		public int getRetryCount() {
			return retryCount;
		}
		public void setRetryCount(int retryCount) {
			this.retryCount = retryCount;
		}
		public int getReadTimeout() {
			return readTimeout;
		}
		public void setReadTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
		}
		public boolean isAuthenticationEnabled() {
			return authenticationEnabled;
		}
		public void setAuthenticationEnabled(boolean authenticationEnabled) {
			this.authenticationEnabled = authenticationEnabled;
		}
		public int getConnectionRequestTimeout() {
			return connectionRequestTimeout;
		}
		public void setConnectionRequestTimeout(int connectionRequestTimeout) {
			this.connectionRequestTimeout = connectionRequestTimeout;
		}
		public int getConnectTimeout() {
			return connectTimeout;
		}
		public void setConnectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
		}
        
	}
	
}
