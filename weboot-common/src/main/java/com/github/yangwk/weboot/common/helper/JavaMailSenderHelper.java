package com.github.yangwk.weboot.common.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class JavaMailSenderHelper {
	private static final Logger LOG = LoggerFactory.getLogger(JavaMailSenderHelper.class);

	private JavaMailSender mailSender;

	public JavaMailSenderHelper(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	private boolean preCheck() {
		if (mailSender == null) {
			LOG.debug("no JavaMailSender provider");
			return false;
		}
		return true;
	}

	public void sendTextMail(String sendFrom, List<String> sendTos, String subject, String text) {
		if (!preCheck()) {
			return;
		}
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(sendFrom);
		message.setTo(sendTos.toArray(new String[0]));
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

}