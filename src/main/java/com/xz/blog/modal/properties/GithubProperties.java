package com.xz.blog.modal.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("github")
@Component
public class GithubProperties {

	
	private String clientId;
	private String clientSecret;
	private String authorizeUrl;
	private String accesstokenUrl;
	private String userUrl;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getAuthorizeUrl() {
		return authorizeUrl;
	}
	public void setAuthorizeUrl(String authorizeUrl) {
		this.authorizeUrl = authorizeUrl;
	}
	public String getAccesstokenUrl() {
		return accesstokenUrl;
	}
	public void setAccesstokenUrl(String accesstokenUrl) {
		this.accesstokenUrl = accesstokenUrl;
	}
	public String getUserUrl() {
		return userUrl;
	}
	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	
	@Override
	public String toString() {
		return "GithubProperties [clientId=" + clientId + ", clientSecret=" + clientSecret + ", authorizeUrl="
				+ authorizeUrl + ", accesstokenUrl=" + accesstokenUrl + ", userUrl=" + userUrl + "]";
	}
	
	
	
}
