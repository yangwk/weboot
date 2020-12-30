package com.github.yangwk.weboot.common.configprop;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weboot.web.logging")
public class WebLoggingConfigProperties {
	private boolean enabled;
	private RequestLogging requestLogging;
	private ResponseLogging responseLogging;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public RequestLogging getRequestLogging() {
		return requestLogging;
	}

	public void setRequestLogging(RequestLogging requestLogging) {
		this.requestLogging = requestLogging;
	}

	public ResponseLogging getResponseLogging() {
		return responseLogging;
	}

	public void setResponseLogging(ResponseLogging responseLogging) {
		this.responseLogging = responseLogging;
	}

	public static class RequestLogging {
		private String strategy;
		private boolean includePayload;
		private boolean includeQueryString;
		private boolean includeClientInfo;
		private boolean includeHeaders;
		private int maxPayloadLength;

		public String getStrategy() {
			return strategy;
		}

		public void setStrategy(String strategy) {
			this.strategy = strategy;
		}

		public boolean isIncludePayload() {
			return includePayload;
		}

		public void setIncludePayload(boolean includePayload) {
			this.includePayload = includePayload;
		}

		public boolean isIncludeQueryString() {
			return includeQueryString;
		}

		public void setIncludeQueryString(boolean includeQueryString) {
			this.includeQueryString = includeQueryString;
		}

		public boolean isIncludeClientInfo() {
			return includeClientInfo;
		}

		public void setIncludeClientInfo(boolean includeClientInfo) {
			this.includeClientInfo = includeClientInfo;
		}

		public boolean isIncludeHeaders() {
			return includeHeaders;
		}

		public void setIncludeHeaders(boolean includeHeaders) {
			this.includeHeaders = includeHeaders;
		}

		public int getMaxPayloadLength() {
			return maxPayloadLength;
		}

		public void setMaxPayloadLength(int maxPayloadLength) {
			this.maxPayloadLength = maxPayloadLength;
		}
	}

	public static class ResponseLogging {
		private List<String> availableMediaTypes;
		private String encode;

		public String getEncode() {
			return encode;
		}

		public void setEncode(String encode) {
			this.encode = encode;
		}
		public List<String> getAvailableMediaTypes() {
			return availableMediaTypes;
		}

		public void setAvailableMediaTypes(List<String> availableMediaTypes) {
			this.availableMediaTypes = availableMediaTypes;
		}

	}

}
