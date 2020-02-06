package com.xz.blog.modal.dto;

import com.xz.blog.modal.po.Thumbup;

public class ThumbupDTO {

	private String key;
	private Integer value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static ThumbupDTO from(Thumbup thumbup) {
		ThumbupDTO dto = new ThumbupDTO();
		StringBuilder builder = new StringBuilder();
		builder.append(thumbup.getArticleId()).append("::").append(thumbup.getUserId());
		dto.setKey(builder.toString());
		dto.setValue(thumbup.getStatus());
		return dto;
	}

	public static Thumbup to(ThumbupDTO dto) {
		String key = (String) dto.getKey();
		String[] split = key.split("::");
		Integer aid = Integer.parseInt(split[0]);
		Integer uid = Integer.parseInt(split[1]);
		Integer value = (Integer) dto.getValue();

		Thumbup thumbup = new Thumbup();
		thumbup.setArticleId(aid);
		thumbup.setUserId(uid);
		thumbup.setStatus(value);
		return thumbup;
	}
}
