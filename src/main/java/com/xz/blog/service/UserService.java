package com.xz.blog.service;

import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.vo.FrontResult;

public interface UserService {

	FrontResult getUser(String userPassport, String password);

	void insertUser(User user);

	User getUser(Integer userId);

	void deleteUser(Integer userId);

	FrontResult hasUserByPassport(String passport);

	UserDTO getUserDTO(Integer userId);

	void updateUser(User user) throws Exception;

	void updateUserCover(User user) throws Exception;

	void updateUserAvatar(User user) throws Exception;

}