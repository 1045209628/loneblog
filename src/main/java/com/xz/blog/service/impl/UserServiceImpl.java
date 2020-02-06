package com.xz.blog.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xz.blog.common.UserValidation;
import com.xz.blog.mapper.PictureMapper;
import com.xz.blog.mapper.UserMapper;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.Picture;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.po.UserExample;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.UserService;
import com.xz.blog.util.DataTransferUtil;
import com.xz.blog.util.EncryptUtil;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PictureMapper pictureMapper;

	@Override
	public FrontResult getUser(String userPassport, String password) {
		UserExample userExample = new UserExample();
		String pwd256 = EncryptUtil.encodeSha256(password);
		if (UserValidation.isEmail(userPassport)) {
			userExample.or().andEmailEqualTo(userPassport);
		} else if (UserValidation.isUserName(userPassport)) {
			userExample.or().andUsernameEqualTo(userPassport);
		} else {
			// 都不是，输入错误
			// log.warn("没有该用户或者输入错误");
			return FrontResult.build(FrontResult.FAULT, "没有该用户或者输入错误");
		}
		List<User> userList = userMapper.selectByExample(userExample);
		if (userList == null || userList.isEmpty()) {
			// log.warn("没有该用户");
			return FrontResult.build(FrontResult.FAULT, "没有该用户");
		}

		// 验证用户
		User user = userList.get(0);
		if (!user.getPassword().equals(pwd256)) {
			// log.warn("密码错误");
			return FrontResult.build(FrontResult.FAULT, "密码错误");
		}
		// 激活验证
		if (!StringUtils.isBlank(user.getActivecode()))
			return FrontResult.fault("账户未激活，请激活后重试");

		// 成功
		String avatarId = user.getAvatarId();
		Picture picture = null;
		if (!StringUtils.isBlank(avatarId)) {
			picture = pictureMapper.selectByPrimaryKey(avatarId);
		}
		UserDTO userDTO = DataTransferUtil.userToDTO(user, picture);

		return FrontResult.success(userDTO);

	}

	@Override
	public void insertUser(User user) {
		userMapper.insert(user);
		log.info("注册用户:{}",user.getId());
	}

	@Override
	public User getUser(Integer userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		return user;
	}

	@Override
	public void deleteUser(Integer userId) {
		userMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public FrontResult hasUserByPassport(String passport) {
		if (StringUtils.isBlank(passport))
			return FrontResult.fault("内容为空");
		UserExample example = new UserExample();
		if (UserValidation.isEmail(passport)) {
			example.or().andEmailEqualTo(passport);
		} else if (UserValidation.isUserName(passport)) {
			example.or().andUsernameEqualTo(passport);
		} else {
			return FrontResult.fault("格式错误");
		}
		List<User> users = userMapper.selectByExample(example);
		if (users == null || users.isEmpty()) {
			return FrontResult.success();
		} else {
			User user = users.get(0);
			// TODO 暂时这样，加入缓存进行改造
			if (!StringUtils.isBlank(user.getActivecode())) {
				deleteUser(user.getId());
				return FrontResult.success();
			}
			return FrontResult.fault("用户已注册");
		}
	}

	@Override
	public UserDTO getUserDTO(Integer userId) {
		User user = getUser(userId);
		if(user==null) return null;
		Picture picture = pictureMapper.selectByPrimaryKey(user.getAvatarId());
		UserDTO userDTO = DataTransferUtil.userToDTO(user, picture);
		return userDTO;
	}

	@Override
	public void updateUser(User user) throws Exception {
		userMapper.updateByPrimaryKeySelective(user);
	}
	
	@Override
	public void updateUserCover(User user) throws Exception {
		userMapper.updateByPrimaryKey(user);
	}

	@Override
	public void updateUserAvatar(User user) throws Exception {
		// 修改头像
		User userResult = userMapper.selectByPrimaryKey(user.getId());
		String avatarId = userResult.getAvatarId();
		Picture picture = new Picture();
		picture.setId(avatarId);
		picture.setUserCount(0);
		pictureMapper.updateByPrimaryKeySelective(picture);
		updateUser(user);
		log.info("用户 {} 更新头像", user.getId());
	}

}
