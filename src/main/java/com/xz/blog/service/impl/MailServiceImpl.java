package com.xz.blog.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.xz.blog.common.ExecuteTime;
import com.xz.blog.modal.dto.MailBean;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.properties.DomainProperties;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private DomainProperties domainProperties;

	@Value("${spring.mail.username}")
	private String MAIL_SENDER;

	@Value("${spring.mail.active.subject}")
	private String ACTIVE_MAIL_SUB;

	// 异步执行
	@Override
	@ExecuteTime
	public void sendMailtoActive(UserDTO userDTO) throws MessagingException, InterruptedException {
		MimeMessage mailMessage = null;

		mailMessage = mailSender.createMimeMessage();
		MimeMessageHelper msgHelper = new MimeMessageHelper(mailMessage, true);
		msgHelper.setFrom(MAIL_SENDER);
		MailBean mailBean = constructActiveTemplateMail(userDTO);
		msgHelper.setTo(mailBean.getReceriver());
		msgHelper.setSubject(mailBean.getSubject());
		msgHelper.setText(mailBean.getContent(), true);
		log.info("发送邮件...");
		mailSender.send(mailMessage);
	}

	// TODO
	@Override
	public FrontResult sendMonitorMail() {
		return null;
	}

	private MailBean constructActiveTemplateMail(UserDTO userDTO) {
		Context context = new Context();
		context.setVariable("username", userDTO.getUsername());
		String url = domainProperties.getDomain() + "/passport/active";
		url += "?code=" + userDTO.getActiveCode();
		url += "&id=" + userDTO.getId();
		context.setVariable("url", url);
		String emailContent = templateEngine.process("email/sendActiveCode", context);
		MailBean mail = new MailBean();
		mail.setContent(emailContent);
		mail.setSubject(ACTIVE_MAIL_SUB);
		mail.setReceriver(userDTO.getEmail());
		return mail;
	}

}
