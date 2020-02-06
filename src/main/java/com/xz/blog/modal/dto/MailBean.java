package com.xz.blog.modal.dto;

public class MailBean {
	/**
	 * 邮件接收者
	 */
	private String receriver;
	/**
	 * 邮件主题
	 */
	private String subject;
	/**
	 * 邮件内容
	 */
	private String content;
	public String getReceriver() {
		return receriver;
	}
	public void setReceriver(String receriver) {
		this.receriver = receriver;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
