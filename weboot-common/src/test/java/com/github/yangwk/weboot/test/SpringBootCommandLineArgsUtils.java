package com.github.yangwk.weboot.test;

import org.springframework.core.env.SimpleCommandLinePropertySource;

public class SpringBootCommandLineArgsUtils {

	public static void putInSystemProperty(final String[] args, String... names) {
		if (args == null || args.length == 0 || names == null || names.length == 0) {
			return;
		}
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		for (String name : names) {
			String value = source.getProperty(name);
			if (value != null) {
				System.setProperty(name, value);
			}
		}
	}

}
