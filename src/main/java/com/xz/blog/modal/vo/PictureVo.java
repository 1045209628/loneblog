package com.xz.blog.modal.vo;

public class PictureVo {

	private String id;
	private Integer status;
	private String url;
	private Integer type;
	private String msg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static PictureVo success(String id, String url, Integer type, String msg) {
		return new PictureVo(id, 200, url, type, msg);
	}

	public static PictureVo fault(String msg) {
		return new PictureVo(null, 400, null, null, msg);
	}

	public static PictureVo error(String msg) {
		return new PictureVo(null, 500, null, null, msg);
	}

	public PictureVo(String id, Integer status, String url, Integer type, String msg) {
		super();
		this.id = id;
		this.status = status;
		this.url = url;
		this.type = type;
		this.msg = msg;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
