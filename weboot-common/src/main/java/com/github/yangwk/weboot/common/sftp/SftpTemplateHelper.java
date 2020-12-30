package com.github.yangwk.weboot.common.sftp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

public class SftpTemplateHelper {

	private SftpTemplate sftpTemplate;

	public SftpTemplateHelper(SftpTemplate sftpTemplate) {
		this.sftpTemplate = sftpTemplate;
	}

	private void requireAllNotEmpty(Object... objs) {
		for (Object obj : objs) {
			Objects.requireNonNull(obj);
			if (obj instanceof String) {
				if (StringUtils.isBlank((String) obj)) {
					throw new IllegalArgumentException();
				}
			} else if (obj instanceof File) {
				if (!((File) obj).exists()) {
					throw new IllegalArgumentException();
				}
			}
		}
	}

	public byte[] download(String directory, String fileName) {
		requireAllNotEmpty(directory, fileName);
		return sftpTemplate.execute(channelSftp -> {
			channelSftp.cd(directory);
			try (InputStream in = channelSftp.get(fileName)) {
				return StreamUtils.copyToByteArray(in);
			}
		});
	}
	
	public List<String> list(String directory) {
		requireAllNotEmpty(directory);
		return sftpTemplate.execute(channelSftp -> {
			List<String> fileNameList = new ArrayList<>();
			Vector<?> files = channelSftp.ls(directory);
			Iterator<?> iter = files.iterator();
			while (iter.hasNext()) {
				String fileName = ((ChannelSftp.LsEntry) iter.next()).getFilename();
				if (".".equals(fileName) || "..".equals(fileName)) {
					continue;
				}
				fileNameList.add(fileName);
			}
			return fileNameList;
		});
	}

	private boolean exists(ChannelSftp channelSftp, String path) {
		boolean exists = false;
		try {
			exists = channelSftp.stat(path) != null;
		} catch (SftpException e) {
			exists = false;
		}
		return exists;
	}

	private void mkdirAsNeeded(ChannelSftp channelSftp, String path) throws SftpException {
		String[] pathNodes = path.split("\\/");
		if (path.charAt(0) == '/') {
			channelSftp.cd("/");
		}
		for (String node : pathNodes) {
			if (node.isEmpty()) {
				continue;
			}
			if (!exists(channelSftp, node)) {
				channelSftp.mkdir(node);
			}
			channelSftp.cd(node);
		}
	}

	public void upload(String directory, String fileName, byte[] file, boolean mkdirIfNeeded) {
		requireAllNotEmpty(directory, fileName, file);
		sftpTemplate.execute(channelSftp -> {
			if(mkdirIfNeeded) {
				mkdirAsNeeded(channelSftp, directory);
			}
			channelSftp.cd(directory);
			try (ByteArrayInputStream in = new ByteArrayInputStream(file)) {
				channelSftp.put(in, fileName);
			}
			return null;
		});

	}
	
	public void upload(String directory, String fileName, File file, boolean mkdirIfNeeded) {
		requireAllNotEmpty(directory, fileName, file);
		sftpTemplate.execute(channelSftp -> {
			if(mkdirIfNeeded) {
				mkdirAsNeeded(channelSftp, directory);
			}
			channelSftp.cd(directory);
			try (FileInputStream in = new FileInputStream(file)) {
				channelSftp.put(in, fileName);
			}
			return null;
		});
	}

}
