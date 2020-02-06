package com.xz.blog.modal.dto;

import com.xz.blog.modal.po.Collection;

public class CollectionDTO {

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
	
	public static CollectionDTO from(Collection collection) {
		CollectionDTO dto= new CollectionDTO();
		StringBuilder builder = new StringBuilder();
		builder.append(collection.getArticleId()).append("::").append(collection.getUserId());
		dto.setKey(builder.toString());
		dto.setValue(collection.getStatus());
		return dto;
	}
	
	public static Collection to(CollectionDTO dto) {
		String key = (String) dto.getKey();
		String[] split = key.split("::");
		Integer aid = Integer.parseInt(split[0]);
		Integer uid = Integer.parseInt(split[1]);
		Integer value = (Integer) dto.getValue();

		Collection collection = new Collection();
		collection.setArticleId(aid);
		collection.setUserId(uid);
		collection.setStatus(value);
		return collection;
	}

}
