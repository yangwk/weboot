package com.github.yangwk.weboot.test.controller;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.yangwk.weboot.test.invoke.InvokeMetadataReqDto;
import com.github.yangwk.weboot.test.sftp.WebootSftpTemplateHelper;

@RestController
@RequestMapping("/sftp/test")
public class SftpTestController {
	private static final Logger LOG = LoggerFactory.getLogger(SftpTestController.class);

	@Autowired
	private WebootSftpTemplateHelper webootSftpTemplateHelper;

	@PostMapping("/fire")
	public Object fire(@RequestBody InvokeMetadataReqDto reqDto) {
		Object retval = null;
		try {
			retval = org.apache.commons.lang3.reflect.MethodUtils.invokeMethod(webootSftpTemplateHelper, true, reqDto.getMethod(), reqDto.paramsToRuntimeType());
			if (retval instanceof byte[]) {
				return Collections.singletonMap("content", new String((byte[]) retval, StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return retval;
	}
	
	
	@PostMapping("/concurrentFire")
	public Object concurrentFire(@RequestBody Map<String, String> params) {
		try {
			long startSleepTime = Long.valueOf(params.get("startSleepTime"));			
			final int threadCount = Math.max(Integer.valueOf(params.get("threadCount")), 0);
			final String downloadDirectory = params.get("downloadDirectory");
			final String downloadFileName = params.get("downloadFileName");
			final String uploadDirectory = params.get("uploadDirectory");
			final String uploadFileName = params.get("uploadFileName");
			final String uploadFileLocalPath = params.get("uploadFileLocalPath");
			
			Thread.sleep(startSleepTime);
			
			CountDownLatch countDownLatch = new CountDownLatch(threadCount);
			Thread[] threads = new Thread[threadCount];
			for(int r=0; r<threads.length; r++) {
				threads[r] = new Thread(() -> {
					try {
						LOG.info("begin");
						try(FileInputStream input = new FileInputStream(uploadFileLocalPath)){
							byte[] file = StreamUtils.copyToByteArray(input);
							webootSftpTemplateHelper.upload(uploadDirectory, Thread.currentThread().getName() + uploadFileName, file, true);
						}
						webootSftpTemplateHelper.download(downloadDirectory, Thread.currentThread().getName() + downloadFileName);
					}catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}finally {
						countDownLatch.countDown();
					}
				});
				threads[r].setName(r+"nameof");
			}
			
			for(int r=0; r<threads.length; r++) {
				threads[r].start();
			}
			countDownLatch.await();
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return Collections.singletonMap("content", "success");
	}
	

}
