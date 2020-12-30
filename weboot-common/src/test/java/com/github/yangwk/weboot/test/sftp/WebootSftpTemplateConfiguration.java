package com.github.yangwk.weboot.test.sftp;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.yangwk.weboot.common.sftp.SftpTemplateBuilder;

@Configuration
@EnableConfigurationProperties(WebootSftpConfigProperties.class)
public class WebootSftpTemplateConfiguration {

	@Bean
	public WebootSftpTemplateHelper webootSftpTemplateHelper(WebootSftpConfigProperties webootSftpConfigProperties) {
		return new WebootSftpTemplateHelper(new SftpTemplateBuilder().build(webootSftpConfigProperties));
	}

}
