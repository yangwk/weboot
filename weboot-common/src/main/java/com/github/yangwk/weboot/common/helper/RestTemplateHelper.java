package com.github.yangwk.weboot.common.helper;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateHelper {
	private static final Logger LOG = LoggerFactory.getLogger(RestTemplateHelper.class);

	private RestTemplate restTemplate;

	public RestTemplateHelper(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String postJson(final String url, Map<String, Object> header, final String json) {
		HttpHeaders httpHeaders = new HttpHeaders();
		@SuppressWarnings("deprecation")
		MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
		httpHeaders.setContentType(mediaType);
		Optional.ofNullable(header).ifPresent(hd -> {
			hd.forEach((k, v) -> {
				httpHeaders.add(k, v == null ? null : v.toString());
			});
		});
		HttpEntity<String> entity = new HttpEntity<>(json, httpHeaders);

		Integer statusCode = null;
		String result = null;
		try {
			LOG.info("http -->> url {} json {}", url, json);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
			statusCode = responseEntity.getStatusCodeValue();
			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				result = responseEntity.getBody();
			}
		} finally {
			LOG.info("http <<-- status {} body {}", statusCode, result);
		}

		return result;
	}

}
