package com.xz.blog.service;

import com.xz.blog.modal.dto.AccessTokenDTO;
import com.xz.blog.modal.dto.GitHubUser;

public interface OAtuhService {

	public String getAccessToken(String url,AccessTokenDTO tokenDTO);
	
	public GitHubUser getUser(String url,String token);
}
