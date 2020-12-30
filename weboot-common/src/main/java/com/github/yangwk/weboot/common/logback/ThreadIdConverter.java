package com.github.yangwk.weboot.common.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ThreadIdConverter extends ClassicConverter{

	@Override
	public String convert(ILoggingEvent event) {
		return String.valueOf(Thread.currentThread().getId());
	}

}
