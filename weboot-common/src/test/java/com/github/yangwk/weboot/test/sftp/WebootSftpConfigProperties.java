package com.github.yangwk.weboot.test.sftp;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.yangwk.weboot.common.configprop.BaseSftpConfigProperties;

@ConfigurationProperties(prefix = "weboot.sftp")
public class WebootSftpConfigProperties extends BaseSftpConfigProperties{
}
