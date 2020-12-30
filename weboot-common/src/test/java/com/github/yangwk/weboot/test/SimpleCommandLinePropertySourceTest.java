package com.github.yangwk.weboot.test;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.SimpleCommandLinePropertySource;

public class SimpleCommandLinePropertySourceTest {

	@Test
	public void parse() {
		String[] args = { "-jar", "demo.jar", "--spring.profiles.active=test" };
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		String name = "spring.profiles.active";
		String value = source.getProperty(name);
		System.out.println(value);
	}
}
