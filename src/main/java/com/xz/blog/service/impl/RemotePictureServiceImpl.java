package com.xz.blog.service.impl;

import java.io.InputStream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.xz.blog.util.FtpUtil;

/**
 * 图片发送到远程服务器不在本地存储
 * 
 * @author xz
 *
 */
@Service("RemotePic")
@ConfigurationProperties("ftp")
@Profile("dev")
public class RemotePictureServiceImpl extends AbstractPictureServiceImpl {

	private String server;
	private int port;
	private String username;
	private String password;

	public void setServer(String server) {
		this.server = server;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	protected boolean TrueUpload(String rootPath,String filePath, String fileName, InputStream input) {
		boolean isUpload = FtpUtil.uploadFile(server, port, username, password, rootPath, filePath, fileName, input);
		return isUpload;
	}

	
}
