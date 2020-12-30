package com.github.yangwk.weboot.common.configuration.task;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;

import com.github.yangwk.weboot.common.configprop.MDCConfigProperties;

@Configuration
@AutoConfigureBefore(value= {TaskExecutionAutoConfiguration.class})
@EnableConfigurationProperties(MDCConfigProperties.class)
public class TaskExecutionConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public TaskDecorator taskDecorator(MDCConfigProperties mdcConfigProperties) {
		return new MDCTaskDecorator(mdcConfigProperties.getGlobalTraceIdName());
	}

}
