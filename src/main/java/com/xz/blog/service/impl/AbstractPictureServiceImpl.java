package com.xz.blog.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.xz.blog.common.ExecuteTime;
import com.xz.blog.mapper.PictureMapper;
import com.xz.blog.modal.po.Picture;
import com.xz.blog.modal.vo.PictureVo;
import com.xz.blog.service.PictureService;
import com.xz.blog.util.EncryptUtil;

public abstract class AbstractPictureServiceImpl implements PictureService {

	private static final Logger log = LoggerFactory.getLogger(AbstractPictureServiceImpl.class);

	String rootPath;
	String picUrl;

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Autowired
	PictureMapper pictureMapper;

	public AbstractPictureServiceImpl() {
		super();
	}

	/**
	 * 博客插入后更新图片引用
	 */
	@Override
	public PictureVo updatePicture(Picture picture) {
		pictureMapper.updateByPrimaryKeySelective(picture);
		return PictureVo.success(picture.getId(), picture.getUrl(), picture.getType(), "更新成功");
	}

	@Override
	public PictureVo deletePicture(Picture picture) {

		return null;
	}

	@Override
	public PictureVo insertNetworkPicture(Picture picture) {
		log.info("网络图片入库");
		pictureMapper.insertSelective(picture);
		return PictureVo.success(picture.getId(), picture.getUrl(), picture.getType(), "使用网络图片");
	}

	@Override
	public Picture getPicture(String picId) {
		Picture pic = pictureMapper.selectByPrimaryKey(picId);
		return pic;
	}

	@Override
	public PictureVo handleUpload(MultipartFile file, Integer type, Integer useId) {
		try {
			// 发送到远程服务器
			String preName = file.getOriginalFilename();
			String suffix = preName.substring(preName.lastIndexOf("."));
			String byteStr = new String(file.getBytes());
			String sha256 = EncryptUtil.encodeSha256(byteStr + System.currentTimeMillis());// 加上时间戳 防重
			String fileName = sha256 + suffix;

			String filePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("/yyyy/MM/dd"));
			// log.info(LocalDateTime.now().toString());

			boolean isUpload = TrueUpload(rootPath, filePath, fileName, file.getInputStream());
			if (!isUpload) {
				log.warn("图片上传失败");
				return PictureVo.error("上传失败");
			}
			log.info("上传图片:{}", fileName);
			String url = picUrl + filePath + "/" + fileName;
			// 记录到数据库中
			Picture picture = new Picture();
			picture.setPath(rootPath + filePath + "/" + fileName);
			picture.setUrl(url);
			picture.setType(type);
			picture.setId(sha256);
			picture.setUseId(useId);
			// 用户上传立即确认
			if (type == 1)
				picture.setUserCount(1);
			pictureMapper.insert(picture);
			log.info("图片入库:{}", sha256);
			// 博客上传稍后进行关联
			return PictureVo.success(sha256, url, type, "上传成功");
		} catch (IOException e) {
			log.error("发生异常", e);
			return PictureVo.error("上传失败");
		}
	}

	@ExecuteTime
	protected abstract boolean TrueUpload(String rootPath, String filePath, String fileName, InputStream input)
			throws IOException;

}