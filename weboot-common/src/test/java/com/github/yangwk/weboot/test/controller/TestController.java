package com.github.yangwk.weboot.test.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.yangwk.weboot.test.bean.TestBean;
import com.github.yangwk.weboot.test.invoke.InvokeMetadataReqDto;
import com.github.yangwk.weboot.test.service.AsyncServiceTest;
import com.github.yangwk.weboot.test.service.ConfigPropServiceTest;
import com.github.yangwk.weboot.test.service.DistributedLockServiceTest;

@RestController
@RequestMapping("/test")
public class TestController {
	private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private AsyncServiceTest asyncServiceTest;
	@Autowired
	private ConfigPropServiceTest configPropServiceTest;
	@Autowired
	private DistributedLockServiceTest distributedLockServiceTest;

	@PostMapping("/invoke")
	public Object invoke(@RequestBody InvokeMetadataReqDto reqDto) {
		Object instance = null;
		if ("asyncServiceTest".equals(reqDto.getBean())) {
			instance = asyncServiceTest;
		}else if("configPropServiceTest".equals(reqDto.getBean())) {
			instance = configPropServiceTest;
		}else if("distributedLockServiceTest".equals(reqDto.getBean())) {
			instance = distributedLockServiceTest;
		}

		if (instance != null) {
			try {
				return org.apache.commons.lang3.reflect.MethodUtils.invokeMethod(instance, reqDto.getMethod(), reqDto.paramsToRuntimeType());
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return "";
	}
	
	@PostMapping("/post")
	public Object post(TestBean bean, HttpServletRequest request) {
		String mytest = request.getParameter("mytest");
		LOG.info(mytest);
		return bean;
	}
	

	@PostMapping("/doLock")
	public void doLock() {
		distributedLockServiceTest.doTest();
	}
	
	@GetMapping("/async2Bridge")
	public void async2Bridge() {
		asyncServiceTest.async2Bridge();
	}
	
	@GetMapping("/crossServiceAsync")
	public void crossServiceAsync() {
		asyncServiceTest.crossServiceAsync();
	}
	
}
