
package com.github.yangwk.weboot.common.configuration.web.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.github.yangwk.weboot.common.OrderedConstants;
import com.github.yangwk.weboot.common.configprop.WebInterceptingConfigProperties;
import com.github.yangwk.weboot.common.configprop.WebLoggingConfigProperties;

@Order(OrderedConstants.ServletFilterConstants.LOGGING)
@EnableConfigurationProperties(WebInterceptingConfigProperties.class)
public class LoggingFilter extends AbstractRequestLoggingFilter {
	private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);

	private final String BeforeRequestAttribute = LoggingFilter.class.getName() + "-beforeRequest";

	final String Strategy_PayloadReadFirst = "payload-read-first";
	final String Strategy_PayloadNoOperation = "payload-no-operation";

	final String Encode_Default = "default";

	@Autowired
	private WebLoggingConfigProperties webLoggingConfigProperties;

	private boolean isPayloadReadFirst() {
		return Strategy_PayloadReadFirst.equals(webLoggingConfigProperties.getRequestLogging().getStrategy());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (shouldLog(request)) {
			super.setIncludePayload(webLoggingConfigProperties.getRequestLogging().isIncludePayload());
			super.setIncludeQueryString(webLoggingConfigProperties.getRequestLogging().isIncludeQueryString());
			super.setIncludeClientInfo(webLoggingConfigProperties.getRequestLogging().isIncludeClientInfo());
			super.setIncludeHeaders(webLoggingConfigProperties.getRequestLogging().isIncludeHeaders());
			super.setMaxPayloadLength(webLoggingConfigProperties.getRequestLogging().getMaxPayloadLength());
			if (isPayloadReadFirst()) {
				super.setBeforeMessagePrefix("request [");
			}
			ContentCachingResponseWrapper responseToUse = new ContentCachingResponseWrapper(response);
			try {
				super.doFilterInternal(
						isPayloadReadFirst() ? new WrappedContentCachingRequestWrapper(request) : request,
						responseToUse, filterChain);
			} finally {
				updateResponse(responseToUse);
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

	private void updateResponse(ContentCachingResponseWrapper response) throws IOException {
		boolean canLog = false;
		byte[] content = null;
		try {
			try {
				canLog = StringUtils.isNotBlank(response.getContentType())
						&& Optional.ofNullable(webLoggingConfigProperties.getResponseLogging().getAvailableMediaTypes())
								.orElse(new ArrayList<>()).stream().map(MediaType::parseMediaType)
								.filter(mt -> mt.isCompatibleWith(MediaType.parseMediaType(response.getContentType())))
								.findFirst().isPresent();

				if (canLog && LOG.isInfoEnabled()) {
					content = response.getContentAsByteArray();
				}
			} finally {
				response.copyBodyToResponse();
			}
		} finally {
			if (canLog && LOG.isInfoEnabled() && content != null) {
				String enc = webLoggingConfigProperties.getResponseLogging().getEncode();
				if (Encode_Default.equals(enc)) {
					enc = response.getCharacterEncoding() != null ? response.getCharacterEncoding()
							: StandardCharsets.UTF_8.name();
				}
				LOG.info("response [{}]", new String(content, enc));
			}
		}
	}

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		return webLoggingConfigProperties.isEnabled();
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		request.setAttribute(BeforeRequestAttribute, "beforeRequest");
		LOG.info(message);
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		if (isPayloadReadFirst()) {
			return;
		}
		LOG.info(message);
	}

	@Override
	protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
		if (isPayloadReadFirst()) {
			if (request.getAttribute(BeforeRequestAttribute) == null) {
				return super.createMessage(request, prefix, suffix);
			}
			return null;
		}

		return super.createMessage(request, prefix, suffix);
	}

	private class WrappedContentCachingRequestWrapper extends ContentCachingRequestWrapper {
		private byte[] requestBody;

		private WrappedContentCachingRequestWrapper(HttpServletRequest request) throws IOException {
			super(request);
			try (FastByteArrayOutputStream out = new FastByteArrayOutputStream(1024)) {
				IOUtils.copy(getRequest().getInputStream(), out);
				this.requestBody = out.toByteArray();
			}
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return new WrappedServletInputStream(getRequest().getInputStream());
		}

		@Override
		public byte[] getContentAsByteArray() {
			return this.requestBody;
		}

		private class WrappedServletInputStream extends ServletInputStream {
			private final ServletInputStream si;
			private final ByteArrayInputStream readedInput;

			private WrappedServletInputStream(ServletInputStream si) {
				this.si = si;
				this.readedInput = new ByteArrayInputStream(WrappedContentCachingRequestWrapper.this.requestBody);
			}

			@Override
			public boolean isFinished() {
				return si.isFinished();
			}

			@Override
			public boolean isReady() {
				return si.isReady();
			}

			@Override
			public void setReadListener(ReadListener listener) {
				si.setReadListener(listener);
			}

			@Override
			public int read() throws IOException {
				return readedInput.read();
			}

		}

	}

}
