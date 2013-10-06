DROP TABLE IF EXISTS memos;
CREATE TABLE `memos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `title` text,
  `content` text,
  `is_private` tinyint(4) NOT NULL DEFAULT '0',

  `created_at` datetime NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

  `user` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,

  PRIMARY KEY (`id`)
);
CREATE INDEX `memos_is_private_idx` ON `memos`(`is_private`);
CREATE INDEX `memos_created_at_idx` ON `memos`(`created_at`);
CREATE INDEX `memos_user_idx` ON `memos`(`user`);

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `last_access` datetime,

  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX `users_username_idx` ON `memos`(`username`);
