package com.github.yangwk.weboot.common.configprop;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weboot.web.intercepting")
public class WebInterceptingConfigProperties {
	private boolean enabled;
	private String notSkipUrlAuthHeaderName;
	private String notSkipUrlAuthHeaderValue;
	private List<String> skipUrls;
	
	public String getNotSkipUrlAuthHeaderName() {
		return notSkipUrlAuthHeaderName;
	}

	public void setNotSkipUrlAuthHeaderName(String notSkipUrlAuthHeaderName) {
		this.notSkipUrlAuthHeaderName = notSkipUrlAuthHeaderName;
	}

	public String getNotSkipUrlAuthHeaderValue() {
		return notSkipUrlAuthHeaderValue;
	}

	public void setNotSkipUrlAuthHeaderValue(String notSkipUrlAuthHeaderValue) {
		this.notSkipUrlAuthHeaderValue = notSkipUrlAuthHeaderValue;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getSkipUrls() {
		return skipUrls;
	}

	public void setSkipUrls(List<String> skipUrls) {
		this.skipUrls = skipUrls;
	}

}
