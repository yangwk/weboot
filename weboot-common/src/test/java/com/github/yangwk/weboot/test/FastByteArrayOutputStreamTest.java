package com.github.yangwk.weboot.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import jodd.io.FastByteArrayOutputStream;

public class FastByteArrayOutputStreamTest {

	@Test
	public void test(){
		try(FastByteArrayOutputStream out = new FastByteArrayOutputStream()) {
			out.write("123456".getBytes());
			
			System.out.println(new String(out.toByteArray()));
			
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			out.writeTo(byteOut);
			out.reset();
			
			System.out.println(new String(out.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
