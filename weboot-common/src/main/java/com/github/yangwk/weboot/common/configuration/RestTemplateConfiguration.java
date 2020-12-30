package com.github.yangwk.weboot.common.configuration;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.github.yangwk.weboot.common.configprop.HttpConfigProperties;

@Configuration
@EnableConfigurationProperties(HttpConfigProperties.class)
public class RestTemplateConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, HttpComponentsClientHttpRequestFactory requestFactory) {
		return restTemplateBuilder
				.additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
				.requestFactory(() -> requestFactory)
				.build();
	}

	@Bean
	@ConditionalOnMissingBean
	public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(HttpConfigProperties httpConfigProperties) {
		return new HttpComponentsClientHttpRequestFactory(httpClient(httpConfigProperties));
	}

	private HttpClient httpClient(HttpConfigProperties httpConfigProperties) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT)
						.setAuthenticationEnabled(httpConfigProperties.getRequestConfig().isAuthenticationEnabled())
						.setConnectionRequestTimeout(httpConfigProperties.getRequestConfig().getConnectionRequestTimeout())
						.setConnectTimeout(httpConfigProperties.getRequestConfig().getConnectTimeout())
						.setSocketTimeout(httpConfigProperties.getRequestConfig().getReadTimeout())
						.build())
				.setMaxConnPerRoute(httpConfigProperties.getMaxConnPerRoute())
				.setMaxConnTotal(httpConfigProperties.getMaxConnTotal())
				.setConnectionTimeToLive(httpConfigProperties.getConnTimeToLive(), TimeUnit.MICROSECONDS);

		if (httpConfigProperties.isEvictExpiredAndIdleConnections()) {
			httpClientBuilder.evictExpiredConnections()
					.evictIdleConnections(httpConfigProperties.getMaxIdleTime(), TimeUnit.SECONDS);
		}

		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpConfigProperties.getRequestConfig().getRetryCount(), false,
				Arrays.asList(
						UnknownHostException.class,
						ConnectException.class,
						SSLException.class)) {

			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				boolean canRetry = super.retryRequest(exception, executionCount, context);
				if (canRetry) {
					if ((exception instanceof InterruptedIOException) && !(exception instanceof org.apache.http.conn.ConnectTimeoutException)) {
						canRetry = false;
					}
				}
				if (canRetry) {
					final long retryInterval = httpConfigProperties.getRequestConfig().getRetryInterval();
					if (retryInterval > 0) {
						try {
							Thread.sleep(retryInterval);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				}
				return canRetry;
			}

			@Override
			protected boolean handleAsIdempotent(final HttpRequest request) {
				return true;
			}

		});
		return httpClientBuilder.build();
	}

}
