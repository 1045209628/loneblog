package com.xz.blog.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.xz.blog.common.ExecuteTime;

@Service("LocalPic")
@ConfigurationProperties("pic.local")
@Profile("prod")
public class LocalPictureServiceImpl extends AbstractPictureServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(LocalPictureServiceImpl.class);

	@Override
	protected boolean TrueUpload(String rootPath, String filePath, String fileName, InputStream input)
			throws IOException {
		OutputStream output = null;
		try {
			String fullPath = rootPath + filePath + "/" + fileName;
			File localPic = new File(fullPath);
			if (localPic.exists()) {
				log.warn("图片存在，检查是否重复");
				return false;
			}

			output = FileUtils.openOutputStream(localPic);
			IOUtils.copy(input, output);

			return true;
		} finally {
			if (input != null)
				input.close();
			if (output != null)
				output.close();
		}
	}

}
