package com.github.yangwk.weboot.common.sftp;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.github.yangwk.weboot.common.configprop.BaseSftpConfigProperties.ConnectConfig;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class ChannelSftpFactory extends BasePooledObjectFactory<ChannelSftp> {

	private ConnectConfig connectConfig;

	public ChannelSftpFactory(ConnectConfig connectConfig) {
		super();
		this.connectConfig = connectConfig;
	}

	private Session createSession() throws Exception {
		JSch jsch = new JSch();
		Session session = jsch.getSession(connectConfig.getUsername(), connectConfig.getHost(), connectConfig.getPort());
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
				return connectConfig.getPassword();
			}

			@Override
			public String getPassphrase() {
				return null;
			}
		});
		session.setPassword(connectConfig.getPassword());
		session.connect(connectConfig.getSessionConnectTimeout());
		session.setTimeout(connectConfig.getSessionReadTimeout());
		return session;
	}

	@Override
	public ChannelSftp create() throws Exception {
		Session session = createSession();
		Channel channel = session.openChannel("sftp");
		channel.connect(connectConfig.getSftpConnectTimeout());
		ChannelSftp channelSftp = (ChannelSftp) channel;
		return channelSftp;
	}

	@Override
	public PooledObject<ChannelSftp> wrap(ChannelSftp obj) {
		return new DefaultPooledObject<>(obj);
	}

	@Override
	public void destroyObject(PooledObject<ChannelSftp> p) throws Exception {
		p.getObject().disconnect();
		p.getObject().getSession().disconnect();
	}

	@Override
	public void activateObject(PooledObject<ChannelSftp> p) throws Exception {
		p.getObject().stat("/");
	}

}
