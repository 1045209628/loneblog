package com.xz.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xz.blog.common.ExecuteTime;
import com.xz.blog.modal.dto.AccessTokenDTO;
import com.xz.blog.modal.dto.GitHubUser;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.Picture;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.properties.GithubProperties;
import com.xz.blog.service.OAtuhService;
import com.xz.blog.service.PictureService;
import com.xz.blog.service.UserService;
import com.xz.blog.util.DataTransferUtil;

@Controller
@RequestMapping("/oauth")
public class OAuthController extends SessionController {

	private static final Logger log = LoggerFactory.getLogger(OAuthController.class);

	@Autowired
	private OAtuhService oauthService;

	@Autowired
	private UserService userService;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private GithubProperties githubProperties;

	@RequestMapping("/callback")
	@ExecuteTime
	public String OAuthCallback(String code) {
		try {
			AccessTokenDTO tokenDTO = new AccessTokenDTO();
			tokenDTO.setClient_id(githubProperties.getClientId());
			tokenDTO.setClient_secret(githubProperties.getClientSecret());
			tokenDTO.setCode(code);
			String accessToken = oauthService.getAccessToken(githubProperties.getAccesstokenUrl(), tokenDTO);
			GitHubUser gituser = oauthService.getUser(githubProperties.getUserUrl(), accessToken);

			if (gituser != null) {
				User guser = DataTransferUtil.gitHubUserToUser(gituser);
				User user = userService.getUser(guser.getId());
				Picture pic = null;
				if (user != null) {
					userService.updateUser(guser);
					pic = pictureService.getPicture(guser.getAvatarId());
				} else {
					userService.insertUser(guser);

					pic = new Picture();
					pic.setId(guser.getAvatarId());
					pic.setType(1);
					pic.setUrl(gituser.getAvatar_url());
					pic.setUserCount(1);
					pic.setUseId(gituser.getId());
					pictureService.insertNetworkPicture(pic);
				}
				guser.setId(gituser.getId());
				UserDTO userDTO = DataTransferUtil.userToDTO(guser, pic);
				setSessionUser(userDTO);
			} else {
				log.warn("Github登录失败");
			}
		} catch (Exception e) {
			log.error("Github登录发生异常", e);
		}
		return "redirect:/home";
	}

}
