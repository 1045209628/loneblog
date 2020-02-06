USE `blog`;

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `desc` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `type` int(5) unsigned NOT NULL DEFAULT '0' COMMENT '账户类型 管理员等',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`username`),
  UNIQUE KEY `mail` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `articles`;

CREATE TABLE `articles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(70) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uid` int(10) unsigned NOT NULL COMMENT '作者id',
  `state` int(10) unsigned DEFAULT '0' COMMENT '文章状态',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'html形式文章内容',
  `abstract` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章摘要',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u-t` (`uid`,`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(10) unsigned NOT NULL COMMENT '评论者id',
  `aid` int(10) unsigned NOT NULL COMMENT '所属博客id',
  `reply_id` int(10) unsigned NOT NULL COMMENT '回复的评论id',
  `reply_uid` int(10) unsigned NOT NULL COMMENT '被回复的评论者id',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `state` int(10) unsigned DEFAULT '0' COMMENT '评论状态',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `aid` (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `collections`;

CREATE TABLE `collections` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(10) unsigned NOT NULL COMMENT '收藏者id',
  `aid` int(10) unsigned NOT NULL COMMENT '收藏的博客id',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u-a` (`uid`,`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;