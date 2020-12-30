package com.github.yangwk.weboot.common.sftp;

import com.jcraft.jsch.ChannelSftp;

@FunctionalInterface
public interface ChannelSftpCallback<T> {
	/**
	 * Wrapped Callback
	 * @param channelSftp all operations must do in a ChannelSftp, can not split them to two or more ChannelSftp . 
	 *  Because all the ChannelSftp's context are different .
	 */
	T doInChannelSftp(ChannelSftp channelSftp) throws Exception;
}
