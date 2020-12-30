package com.github.yangwk.weboot.common.configprop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weboot.mdc")
public class MDCConfigProperties {
	private String globalTraceIdName;

	public String getGlobalTraceIdName() {
		return globalTraceIdName;
	}

	public void setGlobalTraceIdName(String globalTraceIdName) {
		this.globalTraceIdName = globalTraceIdName;
	}

}
