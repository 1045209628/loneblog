package com.xz.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xz.blog.modal.po.User;
import com.xz.blog.modal.vo.PictureVo;
import com.xz.blog.service.PictureService;
import com.xz.blog.service.UserService;

@RestController
@RequestMapping("/picture")
public class PictureController extends SessionController {
	
	private static final Logger log = LoggerFactory.getLogger(PictureController.class);

	@Autowired
	private PictureService pictureService;

	@Autowired
	private UserService userService;

	@PostMapping("/upload")
	public PictureVo uploadPic(MultipartFile picture, Integer type,
			@RequestParam(value = "useId", required = false) Integer useId) {
		try {
			if (picture == null || picture.isEmpty()) {
				return PictureVo.fault("文件为空");
			}

			PictureVo result = pictureService.handleUpload(picture, type, useId);
			// 修改头像
			if (type == 1) {
				if (useId == null || useId == 0) {
					return PictureVo.fault("类型不正确 或 未指明用户");
				}
				User user = new User();
				user.setAvatarId(result.getId());
				user.setId(useId);
				userService.updateUserAvatar(user);
				setSessionUser(userService.getUserDTO(useId));
			}
			return result;
		} catch (Exception e) {
			log.error("发生异常",e);
			return PictureVo.error(e.getLocalizedMessage());
		}
	}

}
