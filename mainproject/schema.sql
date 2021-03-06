-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.18-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for babycare
DROP DATABASE IF EXISTS `babycare`;
CREATE DATABASE IF NOT EXISTS `babycare` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci */;
USE `babycare`;

-- Dumping structure for table babycare.children
DROP TABLE IF EXISTS `children`;
CREATE TABLE IF NOT EXISTS `children` (
  `childid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned NOT NULL,
  `name` varchar(250) COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `babytype` int(1) unsigned NOT NULL DEFAULT '0',
  `dob` bigint(20) unsigned NOT NULL,
  `blood` int(1) unsigned DEFAULT NULL,
  `gender` int(1) unsigned DEFAULT NULL,
  `region` int(2) unsigned DEFAULT NULL,
  PRIMARY KEY (`childid`),
  KEY `FKip3bbhfnbkhl98d1omlou2qya` (`userid`),
  CONSTRAINT `FK_children_user` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;


-- Data exporting was unselected.
-- Dumping structure for table babycare.session
DROP TABLE IF EXISTS `session`;
CREATE TABLE IF NOT EXISTS `session` (
  `sessionid` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned NOT NULL,
  `pushid` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `hardwareid` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `platform` varchar(50) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `status` varchar(50) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`sessionid`),
  UNIQUE KEY `hardwareid` (`hardwareid`),
  KEY `FK_session_user` (`userid`),
  CONSTRAINT `FK_session_user` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- Data exporting was unselected.
-- Dumping structure for table babycare.user
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `userid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(250) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `dob` bigint(20) DEFAULT NULL,
  `provider` varchar(250) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
