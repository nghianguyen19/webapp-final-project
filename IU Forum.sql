-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: forum_db
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookmarks`
--

DROP TABLE IF EXISTS `bookmarks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmarks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKavw6fntse0i19exgqcoygqq2n` (`user_id`,`post_id`),
  KEY `FK7nbb4ldgek7ux7y6lu0y4g826` (`post_id`),
  CONSTRAINT `FK7nbb4ldgek7ux7y6lu0y4g826` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FKdbsho2e05w5r13fkjqfjmge5f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmarks`
--

LOCK TABLES `bookmarks` WRITE;
/*!40000 ALTER TABLE `bookmarks` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookmarks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `color` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`),
  UNIQUE KEY `UKoul14ho7bctbefv8jywp5v3i2` (`slug`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'#2563eb','2026-05-27 21:57:59.602856','Nơi thảo luận chung cho tất cả mọi người','General Discussion','general-discussion'),(2,'#16a34a','2026-05-27 21:57:59.673122','Thảo luận về HTML, CSS, JavaScript, Spring Boot, Node.js','Web Development','web-development'),(3,'#f59e0b','2026-05-27 21:57:59.681667','Hỏi đáp bài tập, assignment và project','Study Help','study-help'),(4,'#7c3aed','2026-05-27 21:57:59.685195','Tin tức công nghệ mới nhất','Technology News','technology-news'),(5,'#dc2626','2026-05-27 21:57:59.690702','Chia sẻ ngoài lề, giải trí và đời sống sinh viên','Off Topic','off-topic');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `post_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  KEY `FKh4c7lvsc298whoyd4w9ta25cr` (`post_id`),
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKh4c7lvsc298whoyd4w9ta25cr` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,'Welcome everyone! Feel free to create discussions and ask questions.','2026-05-27 21:58:00.004936',NULL,1,1),(3,'hi','2026-05-27 22:10:17.271708',NULL,2,3),(4,'hi test noti ne be iu\r\n','2026-05-27 23:12:52.812694',NULL,1,6),(5,'tim cac ban tre dam me valorant','2026-05-31 17:09:54.474909',NULL,2,7);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `message` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK599539lym3mnkbqks0u806eac` (`post_id`),
  KEY `FK9y21adhxn0ayjhfocscqox7bh` (`user_id`),
  CONSTRAINT `FK599539lym3mnkbqks0u806eac` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FK9y21adhxn0ayjhfocscqox7bh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'2026-05-27 23:12:52.821734','admin commented on your post: 123 test noti',_binary '',6,2);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_likes`
--

DROP TABLE IF EXISTS `post_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_likes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5l2rj28vw5oj6f7ox746grokg` (`user_id`,`post_id`),
  KEY `FKa5wxsgl4doibhbed9gm7ikie2` (`post_id`),
  CONSTRAINT `FKa5wxsgl4doibhbed9gm7ikie2` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FKkgau5n0nlewg6o9lr4yibqgxj` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_likes`
--

LOCK TABLES `post_likes` WRITE;
/*!40000 ALTER TABLE `post_likes` DISABLE KEYS */;
INSERT INTO `post_likes` VALUES (12,'2026-05-27 23:00:36.495605',5,2);
/*!40000 ALTER TABLE `post_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_locked` bit(1) DEFAULT NULL,
  `is_pinned` bit(1) DEFAULT NULL,
  `slug` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `view_count` int DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqmmso8qxjpbxwegdtp0l90390` (`slug`),
  KEY `FK5lidm6cqbc7u4xhqpxm898qme` (`user_id`),
  KEY `FKijnwr3brs8vaosl80jg9rp7uc` (`category_id`),
  CONSTRAINT `FK5lidm6cqbc7u4xhqpxm898qme` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKijnwr3brs8vaosl80jg9rp7uc` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (1,'This is the official discussion forum for students. You can ask questions, share knowledge, discuss assignments, and connect with other members.','2026-05-27 21:57:59.985090',NULL,_binary '\0',_binary '\0','welcome-to-campus-forum','Welcome to Campus Forum',NULL,11,1,1),(2,'add ig nhau nhe','2026-05-27 21:57:59.992649',NULL,_binary '\0',_binary '\0','xin-chao-minh-la-pho-rat-han-hanh-dc-lam-quen-cac-ban-1780222262123','xin chao minh la pho, rat han hanh dc lam quen cac ban','2026-05-31 17:11:02.123961',5,2,5),(3,'I want to understand how users, posts, categories, and comments are connected in a forum database. Any suggestions?','2026-05-27 21:57:59.998378',NULL,_binary '\0',_binary '\0','need-help-designing-database-relationships','Need help designing database relationships',NULL,4,2,3),(4,'I want to learn','2026-05-27 22:09:35.054387',NULL,_binary '\0',_binary '\0','how-to-design-a-style-css-1779894601709','How to design a style.css','2026-05-27 22:10:01.709640',5,2,2),(5,'123','2026-05-27 22:25:09.511865',NULL,_binary '\0',_binary '\0','test-chuc-nang-nhanh-123-1780222132405','Test chuc nang nhanh 123','2026-05-31 17:08:52.406209',39,2,2),(6,'123','2026-05-27 23:12:37.136194',NULL,_binary '\0',_binary '\0','123-test-noti-1779898357134','123 test noti',NULL,7,2,1),(7,'TÌM ASC RAD ĐỌ AIM, AI TỰ TIN BƯỚC RA ĐÂY','2026-05-30 21:39:26.310968',NULL,_binary '\0',_binary '','tim-doi-valorant-1780151966309','TÌM ĐỐI VALORANT',NULL,10,2,5);
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comment_id` bigint DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  `reporter_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3x8ylsypiesh2gkwdy5ug7qe7` (`comment_id`),
  KEY `FKneu1viyp671jjiwukyfv6dsy` (`post_id`),
  KEY `FKd3qiw2om5d2oh5xb7fbdcq225` (`reporter_id`),
  CONSTRAINT `FK3x8ylsypiesh2gkwdy5ug7qe7` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`),
  CONSTRAINT `FKd3qiw2om5d2oh5xb7fbdcq225` FOREIGN KEY (`reporter_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKneu1viyp671jjiwukyfv6dsy` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reports`
--

LOCK TABLES `reports` WRITE;
/*!40000 ALTER TABLE `reports` DISABLE KEYS */;
INSERT INTO `reports` VALUES (1,'2026-05-27 23:11:07.334505','','Wrong Information','REVIEWED',NULL,1,2);
/*!40000 ALTER TABLE `reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_definitions`
--

DROP TABLE IF EXISTS `role_definitions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_definitions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `display_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhd1no1fwwhha92t7vn1ila08e` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_definitions`
--

LOCK TABLES `role_definitions` WRITE;
/*!40000 ALTER TABLE `role_definitions` DISABLE KEYS */;
INSERT INTO `role_definitions` VALUES (1,'2026-05-30 21:44:35.049195','Administrator role. Can access admin panel.','ADMIN','ROLE_ADMIN'),(2,'2026-05-30 21:44:35.059267','Default member role.','IUER','ROLE_USER');
/*!40000 ALTER TABLE `role_definitions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bio` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `otp_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otp_expired_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'/uploads/avatars/admin-ce4f2ec1-cafe-467b-bf90-e6cd5b575225.png','Administrator of IU Forum.','2026-05-27 21:57:59.787949','admin@gmail.com','IU Forum Administrator','$2a$10$OMFt7cud5o5.ZfnFUa5fIOwQKE322Oli5ueK6TGr7J1p0CZA/99r6','ROLE_ADMIN','ACTIVE','admin',NULL,NULL),(2,'/uploads/avatars/student-8344a54d-bc77-44e4-8eea-3b4e3a4b9b57.jpg','VALORANT PEAK RANK IMO','2026-05-27 21:57:59.876891','mimi@gmail.com','mimi aka pho','$2a$10$.N.LxJPci5Z.SX8vcmQzUu4Z1PcWI3j5nfPsba9NwjiG7OXfIKr.6','ROLE_USER','ACTIVE','pho',NULL,NULL),(4,'/uploads/avatars/khanhduykd8-67c3e7ed-1b94-4f13-8a24-9a15647b0faf.png','FROM IUFBC WITH LOVE','2026-05-30 21:07:13.983694','trankhanhduy080604@gmail.com','Trần Khánh Duy','$2a$10$e0lA023kUPg3b37Iw0Rdtu2dQFoZWw8.2X46DQadgbCYEx9XVlJRC','ROLE_USER','ACTIVE','khanhduykd8',NULL,NULL),(6,'/uploads/avatars/iufbc-f6d3c245-0671-4a65-ae4b-21722c20316b.png','INTERNATIONAL UNIVERSITY FOOTBALL CLUB','2026-05-31 17:01:03.005735','tkduy.fbc@gmail.com','IU FOOTBALL CLUB','$2a$10$VE/3ON1QQiQ1IzoffE781.v4HNopE/aPJYw0mWLUmO5JOCHx/yG/W','ROLE_USER','ACTIVE','iufbc',NULL,NULL),(7,'/images/default-avatar.png','New member of IU Forum.','2026-06-02 10:26:32.524515','mdkzethuonggg@gmail.com','Mai Diep Khanh','$2a$10$m3hdcQcPcKeNzXo2DybfZefHB1nQjgHd3jhZGpsWPvNe.b9jSMEIa','ROLE_USER','ACTIVE','khanhd','610792','2026-06-02 10:36:32.611065'),(8,'/images/default-avatar.png','New member of IU Forum.','2026-06-02 10:27:39.376018','trananhkhoa200411@gmail.com','Mai Diep Khanh','$2a$10$iVaDx5Vsd09pOVSyKEKFW.b1u7Qe0OHH66roHDRuzQp51.UREIB52','ROLE_USER','ACTIVE','mdkhanh','808186','2026-06-02 10:37:39.461713');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-03 20:49:32
