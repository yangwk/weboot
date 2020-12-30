package com.github.yangwk.weboot.common.sftp;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;

import com.github.yangwk.weboot.common.configprop.BaseSftpConfigProperties.ConnectConfig;
import com.jcraft.jsch.ChannelSftp;

public class SftpTemplate implements Closeable, DisposableBean {

	private ObjectPool<ChannelSftp> channelSftpPool;

	public SftpTemplate(ConnectConfig connectConfig, GenericObjectPoolConfig<ChannelSftp> channelPoolConfig) {
		this.channelSftpPool = new GenericObjectPool<>(new ChannelSftpFactory(connectConfig), channelPoolConfig);
	}

	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(channelSftpPool, (Consumer<IOException>) null);
	}

	public <T> T execute(ChannelSftpCallback<T> action) {
		T t = null;
		ChannelSftp channelSftp = null;
		try {
			channelSftp = channelSftpPool.borrowObject();
			t = action.doInChannelSftp(channelSftp);
		} catch (Exception e) {
			throw new RuntimeException("do in ChannelSftp error", e);
		} finally {
			if (channelSftp != null) {
				try {
					channelSftpPool.returnObject(channelSftp);
				} catch (Exception e) {
					// ignored
				}
			}
		}
		return t;
	}

	@Override
	public void destroy() throws Exception {
		IOUtils.closeQuietly(this, (Consumer<IOException>) null);
	}

}
