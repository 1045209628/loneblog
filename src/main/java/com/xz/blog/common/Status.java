package com.xz.blog.common;

public enum Status {
	
	/**
	 * 删除 -1
	 * @return
	 */
	Delete(-1),
	/**
	 * 不可见 0
	 */
	Invisible(0),
	/**
	 * 仅自己可见 1
	 */
	Self(1),
	/**
	 * 公开 2
	 */
	Public(2);
	
	private final int code;
	
	private Status(int code) {
		this.code=code;
	}

	public int getCode() {
		return code;
	}
	
}