CREATE DATABASE  IF NOT EXISTS `onlineforms` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `onlineforms`;
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: onlineforms
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `answermap`
--

DROP TABLE IF EXISTS `answermap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answermap` (
  `response_id` int NOT NULL,
  `form_id` binary(16) NOT NULL,
  `question` varchar(128) NOT NULL,
  `answer` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`response_id`,`form_id`,`question`),
  CONSTRAINT `answermap_ibfk_1` FOREIGN KEY (`response_id`, `form_id`) REFERENCES `response` (`response_id`, `form_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answermap`
--

LOCK TABLES `answermap` WRITE;
/*!40000 ALTER TABLE `answermap` DISABLE KEYS */;
INSERT INTO `answermap` VALUES (0,_binary '≈°@Ä|Ó®ôu[\ Ûö','_csrf','HZSZOt0RDIMn4Xo-e7nW9_5gzIKIT3mCoViRw1swKl7DHAbEea2vDe4jNOcKhx9bGJTixcdS4eC-Kh-vxz6m9mpVTGilJGDy'),(0,_binary '≈°@Ä|Ó®ôu[\ Ûö','Bike','true'),(0,_binary '≈°@Ä|Ó®ôu[\ Ûö','Car','true'),(0,_binary '≈°@Ä|Ó®ôu[\ Ûö','First Name','Hisham'),(0,_binary '≈°@Ä|Ó®ôu[\ Ûö','Vote','Ahmad'),(1,_binary '≈°@Ä|Ó®ôu[\ Ûö','Bike','true'),(1,_binary '≈°@Ä|Ó®ôu[\ Ûö','Car','true'),(1,_binary '≈°@Ä|Ó®ôu[\ Ûö','First Name','Hisham Alkahtani'),(1,_binary '≈°@Ä|Ó®ôu[\ Ûö','Vehicle','Car'),(1,_binary '≈°@Ä|Ó®ôu[\ Ûö','Vote','Ahmad'),(48243650,_binary 'Uö\ﬁ7C@ç_\≈\ZOF∏','_csrf','QW2tj5aA85r4UD3wIIs7U3sUBTERyjTAqg8todrIWGV-iyKDIwudvfKxxvvVZ1uSEKYPYkwgKAgm_wbtm2sZw-r9aVQdshS3'),(798572437,_binary '\›\œ0\ÎM÷ü\’\–	yê)','_csrf','qKGSuMyy4w1wO33j_IogqG3ncdCDfiri6kzDI7VWjmwuG4dWycD2gfnX1WtdD07QxacUzV2GXLHiTUzP2CmhFoxvvF8cKOYw'),(972824226,_binary '≈°@Ä|Ó®ôu[\ Ûö','_csrf','9lhsN6B0AN3crE6oybjs5be_TT3cO00VeRZ3FuEn4o2dJembkjsOBpZNNb7xmSyQr5XYh4faYAW6An04S3RHItZDhLSsR4uj'),(972824226,_binary '≈°@Ä|Ó®ôu[\ Ûö','Bike','true'),(972824226,_binary '≈°@Ä|Ó®ôu[\ Ûö','First Name','Some guy with a bike'),(972824226,_binary '≈°@Ä|Ó®ôu[\ Ûö','Vote','Saud');
/*!40000 ALTER TABLE `answermap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `field` (
  `form_id` binary(16) NOT NULL,
  `title` varchar(128) NOT NULL,
  `field_description` varchar(512) DEFAULT NULL,
  `field_type` enum('TEXT_FIELD','MULTIPLE_CHOICE','CHECKBOXES') DEFAULT NULL,
  `field_order` int DEFAULT NULL,
  PRIMARY KEY (`form_id`,`title`),
  CONSTRAINT `field_ibfk_1` FOREIGN KEY (`form_id`) REFERENCES `form` (`form_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field`
--

LOCK TABLES `field` WRITE;
/*!40000 ALTER TABLE `field` DISABLE KEYS */;
INSERT INTO `field` VALUES (_binary 'π\À\€\—5K\Îö\“gó\\\Ê','Hillo wel','','TEXT_FIELD',1),(_binary 'π\À\€\—5K\Îö\“gó\\\Ê','IDK WHAT TO PUT HERE','asdf','MULTIPLE_CHOICE',2),(_binary 'π\À\€\—5K\Îö\“gó\\\Ê','s','','CHECKBOXES',3),(_binary '≈°@Ä|Ó®ôu[\ Ûö','First Name','The Name You Were Given ATbIRTH','TEXT_FIELD',1),(_binary '≈°@Ä|Ó®ôu[\ Ûö','Vehicle','What DO YOU HAVE?','CHECKBOXES',3),(_binary '≈°@Ä|Ó®ôu[\ Ûö','Vote',NULL,'MULTIPLE_CHOICE',2);
/*!40000 ALTER TABLE `field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_value`
--

DROP TABLE IF EXISTS `field_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `field_value` (
  `form_id` binary(16) NOT NULL,
  `title` varchar(128) NOT NULL,
  `val` varchar(128) NOT NULL,
  `val_order` int DEFAULT NULL,
  PRIMARY KEY (`form_id`,`title`,`val`),
  CONSTRAINT `field_value_ibfk_1` FOREIGN KEY (`form_id`, `title`) REFERENCES `field` (`form_id`, `title`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_value`
--

LOCK TABLES `field_value` WRITE;
/*!40000 ALTER TABLE `field_value` DISABLE KEYS */;
INSERT INTO `field_value` VALUES (_binary 'π\À\€\—5K\Îö\“gó\\\Ê','s','r',2),(_binary 'π\À\€\—5K\Îö\“gó\\\Ê','s','ra',1),(_binary '≈°@Ä|Ó®ôu[\ Ûö','Vehicle','Bike',2),(_binary '≈°@Ä|Ó®ôu[\ Ûö','Vehicle','Car',1),(_binary '≈°@Ä|Ó®ôu[\ Ûö','Vote','Ahmad',1),(_binary '≈°@Ä|Ó®ôu[\ Ûö','Vote','Saud',2);
/*!40000 ALTER TABLE `field_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form`
--

DROP TABLE IF EXISTS `form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `form` (
  `form_id` binary(16) NOT NULL,
  `form_name` varchar(256) DEFAULT NULL,
  `form_description` varchar(1024) DEFAULT NULL,
  `owner_username` varchar(32) NOT NULL,
  PRIMARY KEY (`form_id`),
  KEY `owner_username` (`owner_username`),
  CONSTRAINT `form_ibfk_1` FOREIGN KEY (`owner_username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form`
--

LOCK TABLES `form` WRITE;
/*!40000 ALTER TABLE `form` DISABLE KEYS */;
INSERT INTO `form` VALUES (_binary '\"\›CH‹ªïLÇ\ﬂ˙ê','soem ds','asdf\n','user'),(_binary 'Uö\ﬁ7C@ç_\≈\ZOF∏','It\'s monday','','user'),(_binary 'π\À\€\—5K\Îö\“gó\\\Ê','Some questions to test things work','IDK IDK IDK','user'),(_binary '≈°@Ä|Ó®ôu[\ Ûö','testForm','Some form to test things out','user'),(_binary '∆ù\—qêGæè¡|QKCµ\¬','asdfsssda','s','user'),(_binary '\›\œ0\ÎM÷ü\’\–	yê)','wh','','user');
/*!40000 ALTER TABLE `form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `response`
--

DROP TABLE IF EXISTS `response`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `response` (
  `response_id` int NOT NULL,
  `form_id` binary(16) NOT NULL,
  PRIMARY KEY (`response_id`,`form_id`),
  KEY `form_id` (`form_id`),
  CONSTRAINT `response_ibfk_1` FOREIGN KEY (`form_id`) REFERENCES `form` (`form_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `response`
--

LOCK TABLES `response` WRITE;
/*!40000 ALTER TABLE `response` DISABLE KEYS */;
INSERT INTO `response` VALUES (48243650,_binary 'Uö\ﬁ7C@ç_\≈\ZOF∏'),(0,_binary '≈°@Ä|Ó®ôu[\ Ûö'),(1,_binary '≈°@Ä|Ó®ôu[\ Ûö'),(2,_binary '≈°@Ä|Ó®ôu[\ Ûö'),(972824226,_binary '≈°@Ä|Ó®ôu[\ Ûö'),(798572437,_binary '\›\œ0\ÎM÷ü\’\–	yê)');
/*!40000 ALTER TABLE `response` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `username` varchar(32) NOT NULL,
  `pass` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('user','user');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-14 14:07:01
