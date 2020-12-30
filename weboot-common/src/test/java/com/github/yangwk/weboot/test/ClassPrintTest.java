package com.github.yangwk.weboot.test;

import org.junit.jupiter.api.Test;

public class ClassPrintTest {

	interface A{}
	
	class B implements A{}
	
	@Test
	public void test() {
		A a = new B();
		System.out.println(a.getClass());
	}
}
