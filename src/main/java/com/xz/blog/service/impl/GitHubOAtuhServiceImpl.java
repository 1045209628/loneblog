package com.xz.blog.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xz.blog.common.OkHttpCli;
import com.xz.blog.modal.dto.AccessTokenDTO;
import com.xz.blog.modal.dto.GitHubUser;
import com.xz.blog.service.OAtuhService;

@Service
public class GitHubOAtuhServiceImpl implements OAtuhService {

	private static final Logger log = LoggerFactory.getLogger(GitHubOAtuhServiceImpl.class);

	@Autowired
	private OkHttpCli okhttpCli;

	@Override
	public String getAccessToken(String url, AccessTokenDTO tokenDTO) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		String accessToken = null;
		try {
			json = mapper.writeValueAsString(tokenDTO);
			accessToken = okhttpCli.doPostJson(url, json);
			if (accessToken.contains("error")) {
				throw new RuntimeException("验证码错误");
			}
			return accessToken;
		} catch (JsonProcessingException e) {
			log.error("发生异常", e);
			return null;
		} catch (RuntimeException e) {
			log.error("错误码:{}", accessToken);
			log.error("验证码错误", e);
			return null;
		}

	}

	@Override
	public GitHubUser getUser(String url, String token) {
		if (token == null)
			return null;
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "token "+token);
		String json = okhttpCli.doGet(url,null,map);
		ObjectMapper mapper = new ObjectMapper();
		GitHubUser gituser = null;
		try {
			gituser = mapper.readValue(json, GitHubUser.class);
		} catch (JsonProcessingException e) {
			log.error("发生异常", e);
		}
		return gituser;
	}

}
