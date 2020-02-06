package com.xz.blog.modal.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("app")
@Component
public class DomainProperties {

	

	private String domain;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Override
	public String toString() {
		return "DomainProperties [domain=" + domain + "]";
	}
	
}
