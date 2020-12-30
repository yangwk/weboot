package com.github.yangwk.weboot.test.controller;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.yangwk.weboot.common.helper.RestTemplateHelper;
import com.github.yangwk.weboot.common.utils.JsonUtils;

@RestController
@RequestMapping("/rest/test")
public class RestTestController {
	private static final Logger LOG = LoggerFactory.getLogger(RestTestController.class);

	@Autowired
	private RestTemplateHelper restTemplateHelper;

	@PostMapping("/json")
	public Object json(@RequestBody Map<String, Object> param) {
		if (param.get("sleepTime") != null) {
			long sleepTime = Long.valueOf(param.get("sleepTime").toString());
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return Collections.singletonMap("name", "boy");
	}

	@GetMapping("/fire")
	public Object fire(String sleepTime) {
		String url = "http://localhost:8080/rest/test/json";
		String json = JsonUtils.objectToJson(Collections.singletonMap("sleepTime", sleepTime));
		String result = restTemplateHelper.postJson(url, null, json);
		return Collections.singletonMap("result", result);
	}

}
