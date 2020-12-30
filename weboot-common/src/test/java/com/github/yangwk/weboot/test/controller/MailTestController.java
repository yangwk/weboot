package com.github.yangwk.weboot.test.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.yangwk.weboot.common.helper.JavaMailSenderHelper;

@RestController
@RequestMapping("/mail/test")
public class MailTestController {
	private static final Logger LOG = LoggerFactory.getLogger(MailTestController.class);

	@Autowired
	private JavaMailSenderHelper javaMailSenderHelper;

	@GetMapping("/fire")
	public void fire() {
		String sendFrom = "appreport@giveu.cn";
		sendFrom = null;
		List<String> sendTos = Arrays.asList(new String[] {"yangwenkun@giveu.cn"});
		String subject = "测试邮件";
		String text = "testtest哈里斯积分都";
		javaMailSenderHelper.sendTextMail(sendFrom, sendTos, subject, text);
		LOG.info("sended");
	}

}
