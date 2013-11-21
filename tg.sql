-- MySQL dump 10.10
--
-- Host: localhost    Database: tg
-- ------------------------------------------------------
-- Server version	5.0.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_table`
--

DROP TABLE IF EXISTS `admin_table`;
CREATE TABLE `admin_table` (
  `type` int(11) NOT NULL default '0',
  `value` varchar(50) default NULL,
  PRIMARY KEY  (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `broadcast_event`
--

DROP TABLE IF EXISTS `broadcast_event`;
CREATE TABLE `broadcast_event` (
  `event_id` bigint(20) NOT NULL,
  `broadcast_ids` varchar(10000) default NULL,
  PRIMARY KEY  (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `guide_event`
--

DROP TABLE IF EXISTS `guide_event`;
CREATE TABLE `guide_event` (
  `guide_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `status` tinyint(4) NOT NULL default '0',
  `satisfaction` tinyint(4) NOT NULL default '0',
  `create_time` bigint(20) NOT NULL,
  `event_type` tinyint(4) NOT NULL,
  `start_time` bigint(10) NOT NULL,
  `end_time` bigint(10) NOT NULL,
  `scenic` varchar(20) default NULL,
  `sa_content` varchar(50) NOT NULL default ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `guide_info`
--

DROP TABLE IF EXISTS `guide_info`;
CREATE TABLE `guide_info` (
  `user_id` int(11) NOT NULL,
  `good_at_scenic` varchar(100) NOT NULL default '',
  `birthday` bigint(20) NOT NULL default '0',
  `be_guide_year` int(11) NOT NULL default '0',
  `guide_card_url` varchar(100) NOT NULL default '""',
  `guide_card_id` varchar(20) NOT NULL default '""',
  `location` varchar(50) NOT NULL default '""',
  `city` smallint(11) NOT NULL default '0',
  `evaluate_score` int(11) NOT NULL default '0',
  `evaluate_count` int(11) NOT NULL default '0',
  `status` int(11) NOT NULL,
  `travel_agency` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `invite_event`
--

DROP TABLE IF EXISTS `invite_event`;
CREATE TABLE `invite_event` (
  `event_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `guide_id` int(11) NOT NULL,
  `event_type` tinyint(4) NOT NULL,
  `event_status` tinyint(4) NOT NULL default '0',
  `create_time` bigint(20) NOT NULL,
  `start_time` bigint(20) NOT NULL,
  `end_time` bigint(20) NOT NULL,
  `scenic` varchar(20) NOT NULL default '',
  `satisfaction` tinyint(4) NOT NULL,
  `sa_content` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL,
  `from_id` int(11) NOT NULL,
  `to_id` int(11) NOT NULL,
  `type` int(5) NOT NULL,
  `content` varchar(100) default '',
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_device_token`
--

DROP TABLE IF EXISTS `user_device_token`;
CREATE TABLE `user_device_token` (
  `user_id` int(11) NOT NULL,
  `device_token` varchar(100) NOT NULL,
  `device_type` int(2) NOT NULL,
  PRIMARY KEY  (`user_id`),
  UNIQUE KEY `device_token` (`device_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(10) default NULL,
  `mobile` varchar(20) NOT NULL default '',
  `gender` tinyint(4) NOT NULL default '0',
  `user_type` tinyint(4) NOT NULL default '0',
  `register_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `head_url` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`user_id`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_pwd`
--

DROP TABLE IF EXISTS `user_pwd`;
CREATE TABLE `user_pwd` (
  `user_id` int(11) NOT NULL,
  `md5_pwd` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

