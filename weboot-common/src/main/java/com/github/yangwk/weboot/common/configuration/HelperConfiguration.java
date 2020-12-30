package com.github.yangwk.weboot.common.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import com.github.yangwk.weboot.common.helper.JavaMailSenderHelper;
import com.github.yangwk.weboot.common.helper.RestTemplateHelper;

@Configuration
public class HelperConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RestTemplateHelper restTemplateHelper(RestTemplate restTemplate) {
		return new RestTemplateHelper(restTemplate);
	}

	@AutoConfigureAfter(value= {MailSenderAutoConfiguration.class})
	@Configuration
	public static class MailHelperConfiguration {
		
		@Bean
		@ConditionalOnMissingBean
		public JavaMailSenderHelper javaMailSenderHelper(ObjectProvider<JavaMailSender> javaMailSender) {
			return new JavaMailSenderHelper(javaMailSender.getIfUnique());
		}
	}
	
}
