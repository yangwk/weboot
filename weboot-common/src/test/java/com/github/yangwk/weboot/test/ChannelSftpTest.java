package com.github.yangwk.weboot.test;

import org.junit.jupiter.api.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.UserInfo;

public class ChannelSftpTest {

	@Test
	public void testStat() {
		String username = "fengyun_hebao";
		String host = "10.10.16.88";
		int port = 22;
		String password = "fyhb$Pd123456";

		Session session = null;
		ChannelSftp channelSftp = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setUserInfo(new UserInfo() {

				@Override
				public void showMessage(String message) {
				}

				@Override
				public boolean promptYesNo(String message) {
					return true;
				}

				@Override
				public boolean promptPassword(String message) {
					return true;
				}

				@Override
				public boolean promptPassphrase(String message) {
					return true;
				}

				@Override
				public String getPassword() {
					return password;
				}

				@Override
				public String getPassphrase() {
					return null;
				}
			});
			session.setPassword(password);
			session.connect();
			
			session.sendIgnore();

			Channel channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			SftpATTRS attrs = channelSftp.stat("/");
			System.out.println(attrs.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channelSftp != null) {
				channelSftp.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

}
