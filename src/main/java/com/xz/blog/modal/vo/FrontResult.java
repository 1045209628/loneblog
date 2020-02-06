package com.xz.blog.modal.vo;

public class FrontResult {

	public static final Integer SUCCESS = 200;
	public static final Integer FAULT = 400;
	public static final Integer ERROR = 500;

	private Integer status;
	private String msg;
	private Object data;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public FrontResult(Integer status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public static FrontResult success() {
		return new FrontResult(SUCCESS, "SUCCESS", null);
	}

	public static FrontResult success(String msg) {
		return new FrontResult(SUCCESS, msg, null);
	}

	public static FrontResult success(String msg, Object data) {
		return new FrontResult(SUCCESS, msg, data);
	}

	public static FrontResult success(Object data) {
		return new FrontResult(SUCCESS, "SUCCESS", data);
	}

	public static FrontResult fault(String msg, Object data) {
		return new FrontResult(FAULT, msg, data);
	}

	public static FrontResult fault(Object data) {
		return new FrontResult(FAULT, "FAULT", data);
	}

	public static FrontResult fault(String msg) {
		return new FrontResult(FAULT, msg, null);
	}

	public static FrontResult error(Throwable e) {
		return new FrontResult(ERROR, "ERROR", e);
	}

	public static FrontResult build(Integer status, String msg, Object data) {
		return new FrontResult(status, msg, data);
	}

	public static FrontResult build(Integer status, String msg) {
		return new FrontResult(status, msg, null);
	}

}
