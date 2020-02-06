package com.xz.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import com.xz.blog.common.ExecuteTime;
import com.xz.blog.modal.po.Article;
import com.xz.blog.modal.po.Collection;
import com.xz.blog.modal.properties.DomainProperties;
import com.xz.blog.modal.properties.GithubProperties;
import com.xz.blog.service.BlogService;
import com.xz.blog.service.CollectionService;
import com.xz.blog.service.PictureService;

@SpringBootTest
@ActiveProfiles("prod")
class BlogSystemApplicationTests {

	@Autowired
	BlogService blogService;
	

	@Test
	void testenv() {
		
		Article article = new Article();
		article.setId(20);
		article.setUserId(2);
		try {
			blogService.updateArticle(article);
			System.out.println(article.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
