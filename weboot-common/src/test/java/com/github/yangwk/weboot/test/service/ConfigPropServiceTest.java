package com.github.yangwk.weboot.test.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.github.yangwk.weboot.common.configprop.HttpConfigProperties;
import com.github.yangwk.weboot.common.configprop.LockConfigProperties;
import com.github.yangwk.weboot.common.configprop.MDCConfigProperties;
import com.github.yangwk.weboot.common.configprop.BaseSftpConfigProperties;
import com.github.yangwk.weboot.common.configprop.WebInterceptingConfigProperties;
import com.github.yangwk.weboot.common.configprop.WebLoggingConfigProperties;

@Service
@EnableConfigurationProperties(value= {MDCConfigProperties.class})
public class ConfigPropServiceTest {
	
	@Autowired
	private HttpConfigProperties httpConfigProperties;
	@Autowired
	private LockConfigProperties lockConfigProperties;
	@Autowired
	private MDCConfigProperties mdcConfigProperties;
	@Autowired
	private BaseSftpConfigProperties baseSftpConfigProperties;
	@Autowired
	private WebInterceptingConfigProperties webInterceptingConfigProperties;
	@Autowired
	private WebLoggingConfigProperties webLoggingConfigProperties;
	
	public List<Object> query(){
		return Arrays.asList(new Object[] {httpConfigProperties, lockConfigProperties, mdcConfigProperties, 
				baseSftpConfigProperties, webInterceptingConfigProperties, webLoggingConfigProperties});
	}
	
}
