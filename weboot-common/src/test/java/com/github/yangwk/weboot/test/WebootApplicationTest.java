package com.github.yangwk.weboot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@EnableWebMvc
@SpringBootApplication(scanBasePackages = { "com.github.yangwk.weboot" })
public class WebootApplicationTest {
	
	public static void main(String[] args) {
		SpringApplication.run(WebootApplicationTest.class, args);
	}

}
