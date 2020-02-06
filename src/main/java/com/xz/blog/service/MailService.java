package com.xz.blog.service;

import javax.mail.MessagingException;

import org.springframework.scheduling.annotation.Async;

import com.xz.blog.common.ExecuteTime;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.vo.FrontResult;

public interface MailService {

	@ExecuteTime
	@Async
	void sendMailtoActive(UserDTO userDTO) throws MessagingException,InterruptedException;

	// TODO
	FrontResult sendMonitorMail();

}