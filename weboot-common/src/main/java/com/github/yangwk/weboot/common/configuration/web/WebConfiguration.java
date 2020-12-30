package com.github.yangwk.weboot.common.configuration.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.yangwk.weboot.common.configprop.WebInterceptingConfigProperties;
import com.github.yangwk.weboot.common.configprop.WebLoggingConfigProperties;
import com.github.yangwk.weboot.common.configuration.web.filter.InterceptingFilter;
import com.github.yangwk.weboot.common.configuration.web.filter.LoggingFilter;
import com.github.yangwk.weboot.common.configuration.web.filter.MDCFilter;

/**
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration
 */
@Configuration
public class WebConfiguration {

	@Bean
	@ConditionalOnMissingFilterBean
	public MDCFilter mdcFilter() {
		return new MDCFilter();
	}

	@Configuration
	@ConditionalOnProperty(prefix = "weboot.web.logging", name = "enabled", havingValue = "true", matchIfMissing = false)
	public static class LoggingFilterConfiguration {

		@Bean
		@ConditionalOnMissingFilterBean
		public LoggingFilter loggingFilter() {
			return new LoggingFilter();
		}

	}
	
	@Bean
	@ConditionalOnMissingBean
	public WebLoggingConfigProperties webLoggingConfigProperties() {
		return new WebLoggingConfigProperties();
	}
	

	@Configuration
	@ConditionalOnProperty(prefix = "weboot.web.intercepting", name = "enabled", havingValue = "true", matchIfMissing = false)
	public static class InterceptingFilterConfiguration {

		@Bean
		@ConditionalOnMissingFilterBean
		public InterceptingFilter interceptingFilter() {
			return new InterceptingFilter();
		}

	}
	
	
	@Bean
	@ConditionalOnMissingBean
	public WebInterceptingConfigProperties webInterceptingConfigProperties() {
		return new WebInterceptingConfigProperties();
	}

}
