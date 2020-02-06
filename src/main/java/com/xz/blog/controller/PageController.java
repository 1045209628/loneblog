package com.xz.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping({ "/", "index" })
	public String getIndexPage() {
		return "index";
	}

	@RequestMapping("/home")
	public String getMainPage(Model model) {

		return "home";
	}

	@RequestMapping("/usertest")
	public String getHeaderPage() {
		return "user";
	}

}
