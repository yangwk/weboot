
package com.github.yangwk.weboot.common.configuration.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.yangwk.weboot.common.OrderedConstants;
import com.github.yangwk.weboot.common.configprop.WebInterceptingConfigProperties;

@Order(OrderedConstants.ServletFilterConstants.INTERCEPTING)
@EnableConfigurationProperties(WebInterceptingConfigProperties.class)
public class InterceptingFilter extends OncePerRequestFilter {

	@Autowired
	private WebInterceptingConfigProperties webInterceptingConfigProperties;

	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		if (webInterceptingConfigProperties.isEnabled()) {
			final boolean shouldSkip = Optional.ofNullable(webInterceptingConfigProperties.getSkipUrls()).orElse(new ArrayList<>()).stream()
					.filter(pattern -> pathMatcher.match(pattern, getRequestPath(request))).findFirst().isPresent();

			if (!shouldSkip) {
				final boolean authPass = StringUtils.isNotBlank(webInterceptingConfigProperties.getNotSkipUrlAuthHeaderName()) &&
						StringUtils.isNotBlank(webInterceptingConfigProperties.getNotSkipUrlAuthHeaderValue()) &&
						webInterceptingConfigProperties.getNotSkipUrlAuthHeaderValue().equals(request.getHeader(webInterceptingConfigProperties.getNotSkipUrlAuthHeaderName()));

				if (!authPass) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
			}
		}

		chain.doFilter(request, response);
	}

	private String getRequestPath(HttpServletRequest request) {
		String url = request.getServletPath();

		if (request.getPathInfo() != null) {
			url += request.getPathInfo();
		}

		return url;
	}

}
