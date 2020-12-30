package com.github.yangwk.weboot.common;

import org.springframework.core.Ordered;

public interface OrderedConstants {

	public interface ServletFilterConstants {
		static final int MDC = org.springframework.boot.web.servlet.filter.OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER + 1;
		static final int LOGGING = org.springframework.boot.web.servlet.filter.OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER + 2;
		static final int INTERCEPTING = org.springframework.boot.web.servlet.filter.OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER + 3;
	}

	public interface AopConstants {
		static final int DISTRIBUTED_LOCK = Ordered.LOWEST_PRECEDENCE;
	}

}
