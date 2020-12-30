
package com.github.yangwk.weboot.common.configuration.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.yangwk.weboot.common.OrderedConstants;
import com.github.yangwk.weboot.common.configprop.MDCConfigProperties;
import com.github.yangwk.weboot.common.utils.MDCUtils;

@Order(OrderedConstants.ServletFilterConstants.MDC)
@EnableConfigurationProperties(MDCConfigProperties.class)
public class MDCFilter extends OncePerRequestFilter {
	
	@Autowired
	private MDCConfigProperties mdcConfigProperties;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
			MDCUtils.generateTraceIdIfNeed(mdcConfigProperties.getGlobalTraceIdName());
			
			chain.doFilter(request, response);
		}finally {
			MDC.clear();
		}
	}

}
