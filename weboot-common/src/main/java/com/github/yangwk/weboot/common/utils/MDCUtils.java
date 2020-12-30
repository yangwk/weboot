package com.github.yangwk.weboot.common.utils;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public final class MDCUtils {
	private MDCUtils() {
	}

	public static void generateTraceIdIfNeed(String name) {
		String traceId = MDC.get(name);
		if (traceId == null) {
			if(StringUtils.isBlank(name)) {
				throw new IllegalArgumentException();
			}
			MDC.put(name, UUID.randomUUID().toString());
		}
	}
}
