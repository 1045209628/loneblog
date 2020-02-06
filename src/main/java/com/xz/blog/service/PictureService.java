package com.xz.blog.service;

import org.springframework.web.multipart.MultipartFile;

import com.xz.blog.modal.po.Picture;
import com.xz.blog.modal.vo.PictureVo;

public interface PictureService {

	/**
	 * 上传
	 * 
	 * @param file
	 * @return
	 */
	PictureVo handleUpload(MultipartFile file, Integer type, Integer id);
	
	
	/**
	 * 使用网络图片
	 * @param picture
	 * @return
	 */
	PictureVo insertNetworkPicture(Picture picture);

	/**
	 * 使用
	 * 
	 * @param picture
	 * @return
	 */
	public PictureVo updatePicture(Picture picture);

	
	public Picture getPicture(String picId);
	
	/**
	 * 删除
	 * quatzz 定时删除
	 * @param picture
	 * @return
	 */
	public PictureVo deletePicture(Picture picture);

}
