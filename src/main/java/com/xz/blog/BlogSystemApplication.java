package com.xz.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.xz.blog.mapper")
@SpringBootApplication
@EnableAsync
public class BlogSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogSystemApplication.class, args);
	}
}
